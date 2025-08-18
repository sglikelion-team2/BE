package wooribe.zarit.service;

import org.springframework.stereotype.Service;
import wooribe.zarit.domain.Pin;
import wooribe.zarit.domain.Pin_environment;
import wooribe.zarit.domain.Photo;
import wooribe.zarit.domain.User;
import wooribe.zarit.dto.response.*;
import wooribe.zarit.repository.PinRepository;
import wooribe.zarit.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class PinService {

    private final PinRepository pinRepository;
    private final UserRepository userRepository;

    public PinService(PinRepository pinRepository, UserRepository userRepository) {
        this.pinRepository = pinRepository;
        this.userRepository = userRepository;
    }

    public MainPageResponseDto getMainPageInfo(String userName, double lat, double lng) {
        User user = userRepository.findByName(userName)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자가 없음!!"));

        // 위치 기반으로 1km 이내의 핀 정보 조회
        List<Pin> nearbyPins = pinRepository.findPinsWithinDistance(lat, lng, 1.0); // 1.0km

        List<Long> top5PinIds = pinRepository.findTop5ByOrderByIdAsc().stream()
                .map(Pin::getId)
                .collect(Collectors.toList());

        List<MainPagePinDto> mainPagePins = new ArrayList<>();
        for (Pin pin : nearbyPins) {
            int rank = 0;
            int top5Index = top5PinIds.indexOf(pin.getId());
            if (top5Index != -1) {
                rank = top5Index + 1; 
            }
            mainPagePins.add(new MainPagePinDto(pin, rank));
        }

        return new MainPageResponseDto(user.getName(), user.getPoint(), mainPagePins);
    }

    public Top5CafeResponseDto getTop5Cafes() {
        List<Pin> pins = pinRepository.findTop5ByOrderByIdAsc();
        List<CafeSummaryDto> cafeSummaries = new ArrayList<>();
        int rank = 1;
        for (Pin pin : pins) {
            cafeSummaries.add(new CafeSummaryDto(pin, rank++));
        }
        return new Top5CafeResponseDto("haechan", cafeSummaries);
    }

    public CafeDetailResponseDto getCafeDetail(String pinName) {
        Pin pin = pinRepository.findByName(pinName)
                .orElseThrow(() -> new NoSuchElementException("해당하는 카페를 찾을 수 없습니다."));

        int noise = 0;
        int wifi = 0;
        int congestion = 0;
        Pin_environment pinEnvironment = pin.getPin_environment();
        if (pinEnvironment != null) {
            noise = (int) pinEnvironment.getNoise();
            wifi = (int) pinEnvironment.getWifi();
            congestion = (int) pinEnvironment.getCongestion();
        }

        List<String> imageUrls = pin.getPhotos().stream()
                .filter(photo -> photo.getIs_cafe() != null && photo.getIs_cafe())
                .map(Photo::getPhoto)
                .collect(Collectors.toList());

        return new CafeDetailResponseDto(pin, noise, wifi, congestion, imageUrls);
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
}
