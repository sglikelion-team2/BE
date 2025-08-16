package wooribe.zarit.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class QuestResponse {
    private final String quest;
    private final int point;

    public static QuestResponse of(String questName, int point) {
        return QuestResponse.builder()
                .quest(questName)
                .point(point)
                .build();
    }


}
