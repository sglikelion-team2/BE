package wooribe.zarit.dto.response;

import lombok.Getter;
import wooribe.zarit.domain.Pin;

@Getter
public class MainPagePinDto {

    private final String pinname;
    private final double lat;
    private final double lng;
    private final int congestion; // 0: 여유, 1: 보통, 2: 혼잡
    private final int rank;

    public MainPagePinDto(Pin pin, int rank, int congestion) {
        this.pinname = pin.getName();
        this.lat = pin.getLat();
        this.lng = pin.getLng();
        this.rank = rank;
        this.congestion = congestion;
    }
}
