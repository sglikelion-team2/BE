package wooribe.zarit.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooribe.zarit.domain.Pin;
import wooribe.zarit.domain.Pin_environment;
import wooribe.zarit.dto.ai.CongestionResponseDto;
import wooribe.zarit.repository.PinEnvironmentRepository;
import wooribe.zarit.repository.PinRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CongestionBatchService {

    private static final String BATCH_IMAGE_FOLDER = "uploads/congestion-batch/";

    private final AiApiService aiApiService;
    private final PinRepository pinRepository;
    private final PinEnvironmentRepository pinEnvironmentRepository;

    /**
     * 서버 시작 10초 후 첫 실행, 이후 매시간 주기적으로 실행되는 혼잡도 분석 배치 작업
     */
    @Scheduled(initialDelay = 10000, fixedRate = 3600000) // 서버 시작 10초 후 실행, 이후 1시간(3600000ms)마다 반복
    @Transactional
    public void runHourlyCongestionAnalysis() {
        int currentHour = LocalDateTime.now().getHour();
        log.info("{}시 혼잡도 분석 배치를 시작합니다.", currentHour);

        File folder = new File(BATCH_IMAGE_FOLDER);
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith("_".concat(String.valueOf(currentHour)).concat(".jpg")));

        if (files == null || files.length == 0) {
            log.info("{}시에 해당하는 분석 대상 이미지가 없습니다.", currentHour);
            return;
        }

        for (File file : files) {
            try {
                String pinName = file.getName().substring(0, file.getName().lastIndexOf('_'));
                log.info("{} 카페의 혼잡도 분석을 시작합니다. (파일: {})", pinName, file.getName());

                byte[] imageBytes = Files.readAllBytes(file.toPath());

                // 1. AI 서비스 호출
                CongestionResponseDto analysisResult = aiApiService.analyzeCongestion(imageBytes);

                // 2. Pin 및 Pin_environment 조회 (없으면 생성)
                Pin pin = pinRepository.findByName(pinName)
                        .orElseThrow(() -> new IllegalArgumentException("파일에 해당하는 Pin을 찾을 수 없습니다: " + pinName));

                Pin_environment pinEnv = pinEnvironmentRepository.findById(pin.getId())
                        .orElseGet(() -> {
                            log.info("{}에 대한 Pin_environment가 없어 새로 생성합니다.", pinName);
                            Pin_environment newEnv = Pin_environment.builder().pin(pin).build();
                            return pinEnvironmentRepository.save(newEnv);
                        });

                // 3. 혼잡도 값 파싱 및 업데이트
                double congestionValue = parseCongestion(analysisResult.getCongestion());
                pinEnv.updateCongestion(congestionValue);

                log.info("{} 카페의 혼잡도 분석 완료: 총 좌석 {}, 채워진 좌석 {}, 혼잡도 {}%",
                        pinName, analysisResult.getTotalSeats(), analysisResult.getOccupiedSeats(), congestionValue);

            } catch (IOException e) {
                log.error("파일을 읽는 중 오류가 발생했습니다: {}", file.getName(), e);
            } catch (Exception e) {
                log.error("{} 파일 처리 중 알 수 없는 오류가 발생했습니다.", file.getName(), e);
            }
        }
        log.info("{}시 혼잡도 분석 배치를 종료합니다.", currentHour);
    }

    private double parseCongestion(String congestionString) {
        if (congestionString == null || !congestionString.endsWith("%")) {
            return 0.0;
        }
        try {
            return Double.parseDouble(congestionString.replace("%", "").trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
