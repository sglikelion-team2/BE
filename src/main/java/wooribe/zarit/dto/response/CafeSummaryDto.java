package wooribe.zarit.dto.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import wooribe.zarit.domain.Pin;
import wooribe.zarit.domain.Pin_environment;

import java.time.format.DateTimeFormatter;
import java.util.Map;

@Getter
public class CafeSummaryDto {

    private final int rank;
    private final String pinname;
    private final double lat;
    private final double lng;
    private final String category; // int -> String으로 변경
    private final boolean is_partnered;
    private final String address;
    private final Map<String, Integer> seat;
    private final int noise;
    private final int wifi;
    private final double rate;
    private final int congestion;
    private final String open_hour;
    private final String close_hour;
    private final String img_url;

    public CafeSummaryDto(Pin pin, int rank) {
        this.rank = rank;
        this.pinname = pin.getName();
        this.lat = pin.getLat();
        this.lng = pin.getLng();
        this.category = pin.getCategory(); // 타입 변경으로 인해 자동 매핑
        this.is_partnered = pin.getIs_partnered();
        this.address = pin.getAddress();
        this.seat = parseSeatInfo(pin.getSeat());
        this.rate = pin.getRate();
        this.open_hour = pin.getOpen_hour().format(DateTimeFormatter.ofPattern("HH:mm"));
        this.close_hour = pin.getClose_hour().format(DateTimeFormatter.ofPattern("HH:mm"));

        Pin_environment env = pin.getPin_environment();
        if (env != null) {
            this.noise = (int) env.getNoise();
            this.wifi = (int) env.getWifi();
            this.congestion = (int) env.getCongestion();
        } else {
            this.noise = 0;
            this.wifi = 0;
            this.congestion = 0;
        }

        if (pin.getPhotos() != null && !pin.getPhotos().isEmpty()) {
            this.img_url = pin.getPhotos().get(0).getPhoto(); // 첫 번째 사진을 대표 이미지로 사용
        } else {
            this.img_url = null; // 사진이 없을 경우 null
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
            e.printStackTrace();
            return null;
        }
    }
}
