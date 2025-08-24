package wooribe.zarit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import wooribe.zarit.domain.*;
import wooribe.zarit.dto.request.NoiseRequest;
import wooribe.zarit.dto.request.PlugbarRequest;
import wooribe.zarit.dto.request.WifiRequest;
import wooribe.zarit.dto.response.QuestResponse;
import wooribe.zarit.repository.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestService {
    @Value("${application.url}")
    private String serverAddress;

    private final UserRepository userRepository;
    private final PinNoiseRepository pinNoiseRepository;
    private final PinWifiRepository pinWifiRepository;
    private final PinPlugbarRepository pinPlugbarRepository;
    private final PinRepository pinRepository;
    private final PinEnvironmentRepository pinEnvironmentRepository;
    private final PinPhotoRepository pinPhotoRepository;
    private final PhotoRepository photoRepository; // PhotoRepository 주입 추가
    private final LocalFileStorageService fileStorageService;

    // 소음 퀘스트
    @Transactional
    public QuestResponse completeNoiseQuest(String userName, NoiseRequest request ) {
        // 사용자 찾기
        User user =userRepository.findByName(userName)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        //Pin 찾기
        Pin pin = pinRepository.findByName(request.getPin_name())
                .orElseThrow(() -> new IllegalArgumentException("핀을 찾을 수 없습니다."));

        // Pin_environment 찾기
        Pin_environment pinEnvironment = pinEnvironmentRepository.findById(pin.getId())
                .orElseGet(() -> {
                            // 없으면 새로운 Pin_environment 엔티티를 만들어서 반환
                            Pin_environment newEnv = Pin_environment.builder()
                                    .pin(pin)
                                    .noise(0.0)
                                    .wifi(0.0)
                                    .plugbar(0.0)
                                    .congestion(0.0)
                                    .build();
                            pinEnvironmentRepository.save(newEnv);
                            return newEnv;
                });

        // 새로운 소음 정보 저장
        Pin_noise newPinNoise = new Pin_noise(pin, request.getNoise());
        pinNoiseRepository.save(newPinNoise);

        // 새롭게 소음 평균 정보 계산
        List<Pin_noise> allPinNoises = pinNoiseRepository.findByPinName(pin.getName());
        double averageNoise = allPinNoises.stream()
                .mapToInt(Pin_noise::getNoise)
                .average()
                .orElse(0.0);

        // 평균 업데이트
        pinEnvironment.updateNoise(averageNoise);

        int points = 10;
        user.addPoint(points);

        return QuestResponse.of("noise", points);
    }

    // 와이파이 퀘스트
    @Transactional
    public QuestResponse completeWifiQuest(String userName, WifiRequest request ) {
        User user =userRepository.findByName(userName)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Pin pin = pinRepository.findByName(request.getPin_name())
                .orElseThrow(() -> new IllegalArgumentException("핀을 찾을 수 없습니다."));

        // Pin_environment 찾기
        Pin_environment pinEnvironment = pinEnvironmentRepository.findById(pin.getId())
                .orElseGet(() -> {
                    Pin_environment newEnv = Pin_environment.builder()
                            .pin(pin)
                            .noise(0.0)
                            .wifi(0.0)
                            .plugbar(0.0)
                            .congestion(0.0)
                            .build();
                    pinEnvironmentRepository.save(newEnv);
                    return newEnv;
                });

        Pin_wifi newPinWifi = new Pin_wifi(pin, request.getWifi());
        pinWifiRepository.save(newPinWifi);

        List<Pin_wifi> allPinWifi = pinWifiRepository.findByPinName(pin.getName());
        double averageWifi = allPinWifi.stream()
                .mapToInt(Pin_wifi::getWifi)
                .average()
                .orElse(0.0);

        pinEnvironment.updateWifi(averageWifi);

        int points = 5;
        user.addPoint(points);

        return QuestResponse.of("wifi", points);
    }

    // 콘센트 퀘스트
    @Transactional
    public QuestResponse completePlugbarQuest(String userName, PlugbarRequest request ) {
        User user =userRepository.findByName(userName)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Pin pin = pinRepository.findByName(request.getPin_name())
                .orElseThrow(() -> new IllegalArgumentException("핀을 찾을 수 없습니다."));

        Pin_environment pinEnvironment = pinEnvironmentRepository.findById(pin.getId())
                .orElseGet(() -> {
                    Pin_environment newEnv = Pin_environment.builder()
                            .pin(pin)
                            .noise(0.0)
                            .wifi(0.0)
                            .plugbar(0.0)
                            .congestion(0.0)
                            .build();
                    pinEnvironmentRepository.save(newEnv);
                    return newEnv;
                });

        Pin_plugbar newPinPlugbar = new Pin_plugbar(pin, request.getPlugbar());
        pinPlugbarRepository.save(newPinPlugbar);

        List<Pin_plugbar> allPinPlugbar = pinPlugbarRepository.findByPinName(pin.getName());
        double averagePlugbar = allPinPlugbar.stream()
                .mapToInt(Pin_plugbar::getPlugbar)
                .average()
                .orElse(0.0);

        pinEnvironment.updatePlugbar(averagePlugbar);

        int points = 7;
        user.addPoint(points);

        return QuestResponse.of("plugbar", points);
    }

    // 사진 퀘스트
    @Transactional
    public QuestResponse completePhotoQuest(String userName, String pinName, MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("사진 파일을 첨부해주세요.");
        }
        User user = userRepository.findByName(userName)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Pin pin = pinRepository.findByName(pinName)
                .orElseThrow(() -> new IllegalArgumentException("핀을 찾을 수 없습니다."));

        String relativePath = fileStorageService.saveFile(file, "photos");
         // 예시: 로컬 환경

        String absoluteUrl = serverAddress + relativePath;

        Pin_photo pinPhoto = new Pin_photo();
        pinPhoto.setPin(pin);
        pinPhoto.setPhoto(absoluteUrl); // 절대 경로를 DB에 저장
        pinPhotoRepository.save(pinPhoto);

        Photo newPhoto = Photo.builder()
                .pin(pin)
                .photo(absoluteUrl) // 절대 경로를 DB에 저장
                .is_cafe(false)
                .build();
        photoRepository.save(newPhoto);


        int points = 20;
        user.addPoint(points);

        return QuestResponse.of("photo", points);
    }
}
