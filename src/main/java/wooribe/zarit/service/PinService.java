package wooribe.zarit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooribe.zarit.domain.*;
import wooribe.zarit.dto.response.*;
import wooribe.zarit.repository.PinRepository;
import wooribe.zarit.repository.UserRecommendationRepository;
import wooribe.zarit.repository.UserRepository;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PinService {

    @Value("${application.url}")
    private String serverAddress;

    private final PinRepository pinRepository;
    private final UserRepository userRepository;
    private final AiRecommendationService aiRecommendationService;
    private final UserRecommendationRepository userRecommendationRepository;

    @Transactional
    public MainPageResponseDto getMainPageInfo(String userName, double lat, double lng) {
        User user = userRepository.findByName(userName)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자가 없음!!"));

        // 1. AI 추천 로직을 통해 순위가 매겨진 Pin ID 목록을 가져옴
        List<Pin> rankedPins = getRankedPinsByAi(user, lat, lng);
        List<Long> rankedPinIds = rankedPins.stream().map(Pin::getId).collect(Collectors.toList());

        // 2. AI 추천 결과를 DB에 저장
        UserRecommendation recommendation = user.getUserRecommendation();
        if (recommendation == null) {
            recommendation = UserRecommendation.builder().user(user).rankedPinIds(rankedPinIds).build();
            user.setUserRecommendation(recommendation); // User 엔티티에도 세팅
        } else {
            recommendation.update(rankedPinIds);
        }
        userRecommendationRepository.save(recommendation);

        // 3. 응답 DTO 생성
        List<Pin> nearbyPins = pinRepository.findPinsWithinDistance(lat, lng, 1.0);
        Map<Long, Integer> rankMap = rankedPinIds.stream()
                .limit(5)
                .collect(Collectors.toMap(Function.identity(), id -> rankedPinIds.indexOf(id) + 1));

        List<MainPagePinDto> mainPagePins = nearbyPins.stream().map(pin -> {
            int rank = rankMap.getOrDefault(pin.getId(), 0);
            int congestionLevel = convertCongestionLevel(pin.getPin_environment() != null ? pin.getPin_environment().getCongestion() : 0.0);
            return new MainPagePinDto(pin, rank, congestionLevel);
        }).collect(Collectors.toList());

        return new MainPageResponseDto(user.getName(), user.getPoint(), nearbyPins.size(), mainPagePins);
    }

    @Transactional(readOnly = true)
    public Top5CafeResponseDto getTop5Cafes(String userName,double lat, double lng) {
        User user = userRepository.findByName(userName)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자가 없음!!"));

        // DB에 저장된 최신 추천 결과를 조회
        UserRecommendation recommendation = user.getUserRecommendation();
        if (recommendation == null || recommendation.getRankedPinIds().isEmpty()) {
            return new Top5CafeResponseDto(userName, new ArrayList<>());
        }

        List<Long> top5PinIds = recommendation.getRankedPinIds().stream().limit(5).collect(Collectors.toList());
        Map<Long, Pin> pinMap = pinRepository.findAllById(top5PinIds).stream().collect(Collectors.toMap(Pin::getId, Function.identity()));

        // DB 조회 결과는 순서를 보장하지 않으므로, 추천 순서대로 다시 정렬
        List<CafeSummaryDto> cafeSummaries = top5PinIds.stream()
                .map(pinMap::get)
                .map(pin -> {
                    Pin_environment env = pin.getPin_environment();
                    int noise = (env != null) ? (int) env.getNoise() : 0;
                    int wifi = (env != null) ? (int) env.getWifi() : 0;
                    int congestionLevel = convertCongestionLevel(env != null ? env.getCongestion() : 0.0);
                    int rank = top5PinIds.indexOf(pin.getId()) + 1;
                    return new CafeSummaryDto(pin, rank, noise, wifi, congestionLevel);
                })
                .collect(Collectors.toList());

        return new Top5CafeResponseDto(userName, cafeSummaries);
    }

    private List<Pin> getRankedPinsByAi(User user, double lat, double lng) {
        User_preference preference = user.getUser_preference();
        if (preference == null) throw new NoSuchElementException("사용자 선호도 정보가 없습니다.");

        List<Pin> nearbyPins = pinRepository.findPinsWithinDistance(lat, lng, 1.0);
        LocalTime now = LocalTime.now();
        List<Pin> candidatePins = nearbyPins.stream()
                .filter(pin -> pin.getOpen_hour() != null && pin.getClose_hour() != null)
                .filter(pin -> !now.isBefore(pin.getOpen_hour()) && !now.isAfter(pin.getClose_hour()))
                .collect(Collectors.toList());

        if (candidatePins.isEmpty()) return new ArrayList<>();

        String prompt = buildRecommendationPrompt(preference, candidatePins, lat, lng);
        List<String> rankedCafeNames = aiRecommendationService.getRankedCafes(prompt);

        Map<String, Pin> candidatePinMap = candidatePins.stream().collect(Collectors.toMap(Pin::getName, Function.identity()));
        return rankedCafeNames.stream().map(candidatePinMap::get).collect(Collectors.toList());
    }

    // CafeDetail, PhotoUrls, buildPrompt, calculateDistance, convertCongestionLevel 메서드는 이전과 동일
    public CafeDetailResponseDto getCafeDetail(String pinName) {
        Pin pin = pinRepository.findByName(pinName)
                .orElseThrow(() -> new NoSuchElementException("해당하는 카페를 찾을 수 없습니다."));

        Pin_environment env = pin.getPin_environment();
        int noise = (env != null) ? (int) env.getNoise() : 0;
        int wifi = (env != null) ? (int) env.getWifi() : 0;
        double congestionValue = (env != null) ? env.getCongestion() : 0.0;
        int congestionLevel = convertCongestionLevel(congestionValue);
        //String serverIp = "http://3.39.6.173:80"; // 나중에 배포할 때는 실제 서버 IP로 변경!

        List<String> imageUrls = pin.getPhotos().stream()
                .filter(photo -> photo.getIs_cafe() != null && photo.getIs_cafe())
                .map(Photo::getPhoto) // 이제 변수를 사용!
                .collect(Collectors.toList());
//        List<String> imageUrls = pin.getPhotos().stream()
//                .filter(photo -> photo.getIs_cafe() != null && photo.getIs_cafe())
//                .map(Photo::getPhoto)
//                .collect(Collectors.toList());

        return new CafeDetailResponseDto(pin, noise, wifi, congestionLevel, imageUrls);
    }

    private String buildRecommendationPrompt(User_preference pref, List<Pin> pins, double userLat, double userLng) {
        StringBuilder sb = new StringBuilder();
        sb.append("You are a cafe recommendation expert. Your task is to rank the following list of cafes based on the user's preferences and the cafe's current conditions.\n");
        sb.append("The most important factors are **low congestion** and **short distance**. After considering these, rank the cafes based on how well they match the user's preferences.\n\n");

        sb.append("## User Preferences:\n");
        sb.append(String.format("- Purpose: %s\n- Atmosphere: %s\n- Desired Facility: %s\n\n", pref.getPurpose(), pref.getAtmos(), pref.getFacility()));

        sb.append("## Candidate Cafes:\n");
        for (Pin pin : pins) {
            Pin_environment env = pin.getPin_environment();
            double distance = calculateDistance(userLat, userLng, pin.getLat(), pin.getLng());
            sb.append(String.format("- Cafe Name: %s\n", pin.getName()));
            sb.append(String.format("  - Distance: %.0f meters\n", distance));
            sb.append(String.format("  - Category: %s\n", pin.getCategory()));
            sb.append(String.format("  - Description: %s\n", pin.getPin_info()));
            if (env != null) {
                sb.append(String.format("  - Environment: Noise=%.1f, Wifi=%.1f, Power Outlets=%.1f, Congestion=%.1f%%\n",
                        env.getNoise(), env.getWifi(), env.getPlugbar(), env.getCongestion()));
            }
        }

        sb.append("\nBased on all this information, provide ALWAYS 5 ranked list of the cafe names. 언제나 꼭 5개로 줘야해. 중복된 값 말고. Your response MUST be a JSON array of strings, containing only the names of the cafes in the recommended order. 반드시 순수한 JSON 형식으로만 응답해줘. 다른 설명이나 마크다운은 절대 포함하지 마.For example: [\"cafe_A\", \"cafe_B\", \"cafe_C\",\"cafe_D\",\"cafe_E\"]");
        return sb.toString();
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
        dist = Math.acos(dist);
        dist = Math.toDegrees(dist);
        dist = dist * 60 * 1.1515 * 1609.344; // m 단위
        return dist;
    }

    public PhotoListResponseDto getPhotoUrls(String pinName) {
        Pin pin = pinRepository.findByName(pinName)
                .orElseThrow(() -> new NoSuchElementException("해당하는 카페를 찾을 수 없습니다."));

        List<String> imageUrls = pin.getPhotos().stream()
                .filter(photo -> photo.getIs_cafe() != null && !photo.getIs_cafe())
                .map(Photo::getPhoto)
                .collect(Collectors.toList());

        if (imageUrls.isEmpty()) {
            throw new NoSuchElementException("사진 없음");
        }

        return new PhotoListResponseDto(pin.getName(), imageUrls);
    }

    private int convertCongestionLevel(double congestion) {
        if (congestion < 40) return 0; // 여유
        if (congestion < 80) return 1; // 보통
        return 2; // 혼잡
    }
}
