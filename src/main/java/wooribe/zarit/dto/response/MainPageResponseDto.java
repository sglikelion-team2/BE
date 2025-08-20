package wooribe.zarit.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class MainPageResponseDto {

    private final String name;
    private final int point;
    private final int cnt;
    private final List<MainPagePinDto> pin;

    public MainPageResponseDto(String name, int point,int cnt, List<MainPagePinDto> pin) {
        this.name = name;
        this.point = point;
        this.cnt = cnt;
        this.pin = pin;
        //this.cnt = pin.size(); // pin 목록의 개수를 cnt로 설정
    }
}
