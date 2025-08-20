package wooribe.zarit.dto.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import wooribe.zarit.domain.Pin;
import wooribe.zarit.domain.Photo;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class CafeSummaryDto {

    private final int rank;
    private final String pinname;
    private final double lat;
    private final double lng;
    private final String category;
    private final boolean is_partnered;
    private final String address;
    private final Map<String, Integer> seat;
    private final int noise;
    private final int wifi;
    private final double rate;
    private final int congestion; // 0: 여유, 1: 보통, 2: 혼잡
    private final String open_hour;
    private final String close_hour;
    private final String img_url1;
    private final String img_url2;

    public CafeSummaryDto(Pin pin, int rank, int noise, int wifi, int congestion) {
        this.rank = rank;
        this.pinname = pin.getName();
        this.lat = pin.getLat();
        this.lng = pin.getLng();
        this.category = pin.getCategory();
        this.is_partnered = pin.getIs_partnered();
        this.address = pin.getAddress();
        this.seat = parseSeatInfo(pin.getSeat());
        this.rate = pin.getRate();
        this.open_hour = pin.getOpen_hour().format(DateTimeFormatter.ofPattern("HH:mm"));
        this.close_hour = pin.getClose_hour().format(DateTimeFormatter.ofPattern("HH:mm"));

        this.noise = noise;
        this.wifi = wifi;
        this.congestion = congestion;

        // is_cafe가 true인 사진 목록을 가져옴
        List<String> cafePhotoUrls = pin.getPhotos().stream()
                .filter(photo -> photo.getIs_cafe() != null && photo.getIs_cafe())
                .map(Photo::getPhoto)
                .collect(Collectors.toList());

        // 첫 번째와 두 번째 사진 URL을 할당
        this.img_url1 = cafePhotoUrls.size() > 0 ? cafePhotoUrls.get(0) : null;
        this.img_url2 = cafePhotoUrls.size() > 1 ? cafePhotoUrls.get(1) : null;
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
