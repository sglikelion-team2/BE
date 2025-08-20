package wooribe.zarit.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import wooribe.zarit.config.AiApiProperties;
import wooribe.zarit.dto.ai.AiRequestDto;
import wooribe.zarit.dto.ai.AiResponseDto;
import wooribe.zarit.dto.ai.CongestionResponseDto;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AiApiService {

    private final AiApiProperties aiApiProperties;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 파싱을 위한 ObjectMapper

    private static final String CONGESTION_INSTRUCTION = "당신은 카페 좌석 점유율을 분석하는 전문가입니다. " +
            "주어진 카페 내부 사진을 보고, 전체 좌석 수와 현재 사람이 앉아있는 좌석 수를 세어주세요. " +
            "답변은 반드시 다음 JSON 형식에 맞춰서만 작성해야 합니다: " +
            "`{ \"총 좌석\": nn, \"채워진 좌석\": nn, \"congestion\": \"n%\" }`. " +
            "다른 부가적인 설명은 절대 추가하지 마세요.";

    public CongestionResponseDto analyzeCongestion(byte[] imageBytes) {
        // 1. 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(aiApiProperties.getKey());

        // 2. 이미지 Base64 인코딩 및 요청 컨텐츠 생성
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        String imageUrl = "data:image/png;base64," + base64Image;

        List<AiRequestDto.Content> contentList = new ArrayList<>();
        contentList.add(new AiRequestDto.TextContent("text", CONGESTION_INSTRUCTION));
        contentList.add(new AiRequestDto.ImageUrlContent("image_url", new AiRequestDto.ImageUrl(imageUrl)));

        // 3. 최종 요청 DTO 생성
        AiRequestDto.Message message = new AiRequestDto.Message("user", contentList);
        AiRequestDto requestBody = new AiRequestDto("gpt-5-chat-latest", List.of(message));

        // 4. HTTP 요청 엔티티 생성
        HttpEntity<AiRequestDto> entity = new HttpEntity<>(requestBody, headers);

        // 5. API 호출 및 응답 받기
        try {
            ResponseEntity<AiResponseDto> response = restTemplate.postForEntity(
                aiApiProperties.getUrl(),
                entity,
                AiResponseDto.class
            );

            // 6. AI 응답에서 content(JSON 문자열) 추출
            String jsonContent = Optional.ofNullable(response.getBody())
                    .map(AiResponseDto::getChoices)
                    .filter(choices -> !choices.isEmpty())
                    .map(choices -> choices.get(0))
                    .map(AiResponseDto.Choice::getMessage)
                    .map(AiResponseDto.ResponseMessage::getContent)
                    .map(String::trim)
                    .orElseThrow(() -> new RuntimeException("AI 응답에서 content를 추출할 수 없습니다."));

            // 7. 추출된 JSON 문자열을 DTO로 변환
            return objectMapper.readValue(jsonContent, CongestionResponseDto.class);

        } catch (Exception e) {
            throw new RuntimeException("AI API 호출 또는 응답 처리 중 오류가 발생했습니다.", e);
        }
    }
}
