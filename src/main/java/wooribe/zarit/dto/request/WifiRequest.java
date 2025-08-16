package wooribe.zarit.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WifiRequest {
    private String name;
    private String pin_name;
    private int noise;
}
