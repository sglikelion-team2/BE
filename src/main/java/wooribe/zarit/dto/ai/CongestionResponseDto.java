package wooribe.zarit.dto.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CongestionResponseDto {

    @JsonProperty("총 좌석")
    private int totalSeats;

    @JsonProperty("채워진 좌석")
    private int occupiedSeats;

    @JsonProperty("congestion")
    private String congestion;
}
