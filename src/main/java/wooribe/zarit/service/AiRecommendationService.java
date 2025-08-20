package wooribe.zarit.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wooribe.zarit.config.AiApiProperties;
import wooribe.zarit.dto.ai.AiRequestDto;
import wooribe.zarit.dto.ai.AiResponseDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AiRecommendationService {

    private final AiApiProperties aiApiProperties;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * AI에게 프롬프트를 보내 카페 순위를 추천받는 메서드
     * @param prompt AI에게 전달할 상세한 지침과 정보
     * @return AI가 추천한 카페 이름의 순서가 담긴 리스트
     */
    public List<String> getRankedCafes(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(aiApiProperties.getKey());

        // AI 요청 본문 생성 (이미지 없이 텍스트만 사용)
        List<AiRequestDto.Content> contentList = new ArrayList<>();
        contentList.add(new AiRequestDto.TextContent("text", prompt));

        AiRequestDto.Message message = new AiRequestDto.Message("user", contentList);
        AiRequestDto requestBody = new AiRequestDto("gpt-5-chat-latest", List.of(message));

        HttpEntity<AiRequestDto> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<AiResponseDto> response = restTemplate.postForEntity(
                aiApiProperties.getUrl(),
                entity,
                AiResponseDto.class
            );

            String jsonContent = Optional.ofNullable(response.getBody())
                    .map(AiResponseDto::getChoices)
                    .filter(choices -> !choices.isEmpty())
                    .map(choices -> choices.get(0))
                    .map(AiResponseDto.Choice::getMessage)
                    .map(AiResponseDto.ResponseMessage::getContent)
                    .map(String::trim)
                    .orElseThrow(() -> new RuntimeException("AI 응답에서 content를 추출할 수 없습니다."));

            // AI가 반환한 JSON 배열 문자열을 List<String>으로 변환
            System.out.println(jsonContent);
            return objectMapper.readValue(jsonContent, new TypeReference<List<String>>() {});

        } catch (Exception e) {
            throw new RuntimeException("AI 추천 API 호출 또는 응답 처리 중 오류가 발생했습니다.", e);
        }
    }
}
