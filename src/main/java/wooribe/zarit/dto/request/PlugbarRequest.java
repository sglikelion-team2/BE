package wooribe.zarit.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlugbarRequest {
    private String name;
    private String pin_name;

    @Min(value = 1, message = "플러그바 값은 필수입니다.")
    private int plugbar;
}
