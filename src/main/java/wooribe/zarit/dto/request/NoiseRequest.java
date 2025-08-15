package wooribe.zarit.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoiseRequest {
    private String name;
    private String pin_name;
    private int noise;
}
