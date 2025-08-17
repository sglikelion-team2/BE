package wooribe.zarit.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class PhotoListResponseDto {

    private final String pin_name;
    private final List<String> img_url;

    public PhotoListResponseDto(String pin_name, List<String> img_url) {
        this.pin_name = pin_name;
        this.img_url = img_url;
    }
}
