package wooribe.zarit.dto.response;

import lombok.Getter;
import wooribe.zarit.domain.Pin;
import wooribe.zarit.domain.Pin_environment;

@Getter
public class MainPagePinDto {

    private final String pinname;
    private final double lat;
    private final double lng;
    private final int congestion;
    private final int rank;

    public MainPagePinDto(Pin pin, int rank) {
        this.pinname = pin.getName();
        this.lat = pin.getLat();
        this.lng = pin.getLng();
        this.rank = rank;

        Pin_environment env = pin.getPin_environment();
        if (env != null) {
            this.congestion = (int) env.getCongestion();
        } else {
            this.congestion = 0; // 정보가 없을 경우 기본값 '여유'로 설정
        }
    }
}
