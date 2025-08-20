package wooribe.zarit.dto.ai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // 응답의 모든 필드를 매핑할 필요 없으므로, 모르는 필드는 무시하도록 설정
public class AiResponseDto {
    private List<Choice> choices;

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Choice {
        private ResponseMessage message;
    }

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ResponseMessage {
        private String content;
    }
}
