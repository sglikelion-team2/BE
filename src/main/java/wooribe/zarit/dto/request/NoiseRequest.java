package wooribe.zarit.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoiseRequest {
    private String name;
    private String pin_name;

    @Min(value = 1, message = "소음 값은 필수입니다.")
    private int noise;
}
