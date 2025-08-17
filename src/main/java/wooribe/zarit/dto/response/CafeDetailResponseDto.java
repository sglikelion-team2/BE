package wooribe.zarit.dto.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import wooribe.zarit.domain.Pin;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Getter
public class CafeDetailResponseDto {

    private final String name;
    private final double lat;
    private final double lng;
    private final String category; // int -> String으로 변경
    private final boolean is_partnered;
    private final String address;
    private final String pin_info;
    private final Map<String, Integer> seat;
    private final int noise;
    private final int wifi;
    private final double rate;
    private final int congestion;
    private final String open_hour;
    private final String close_hour;
    private String img_url_1;
    private String img_url_2;

    public CafeDetailResponseDto(Pin pin, int noise, int wifi, int congestion, List<String> imageUrls) {
        this.name = pin.getName();
        this.lat = pin.getLat();
        this.lng = pin.getLng();
        this.category = pin.getCategory(); // 타입 변경으로 인해 자동 매핑
        this.is_partnered = pin.getIs_partnered();
        this.address = pin.getAddress();
        this.pin_info = pin.getPin_info();
        this.seat = parseSeatInfo(pin.getSeat());
        this.noise = noise;
        this.wifi = wifi;
        this.rate = pin.getRate();
        this.congestion = congestion;
        this.open_hour = pin.getOpen_hour().format(DateTimeFormatter.ofPattern("HH:mm"));
        this.close_hour = pin.getClose_hour().format(DateTimeFormatter.ofPattern("HH:mm"));

        if (imageUrls != null && !imageUrls.isEmpty()) {
            this.img_url_1 = imageUrls.get(0);
            if (imageUrls.size() > 1) {
                this.img_url_2 = imageUrls.get(1);
            }
        }
    }

    private Map<String, Integer> parseSeatInfo(String seatJson) {
        if (seatJson == null || seatJson.isEmpty()) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(seatJson, new TypeReference<Map<String, Integer>>() {});
        } catch (JsonProcessingException e) {
            // 나중에 로깅 등으로 처리하면 좋습니다.
            e.printStackTrace();
            return null;
        }
    }
}
