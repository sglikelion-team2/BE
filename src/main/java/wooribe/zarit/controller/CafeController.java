package wooribe.zarit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooribe.zarit.dto.response.*;
import wooribe.zarit.service.PinService;

import java.util.NoSuchElementException;

@RestController
public class CafeController {

    private final PinService pinService;

    public CafeController(PinService pinService) {
        this.pinService = pinService;
    }

    @GetMapping("/mainpage/{name}")
    public ResponseEntity<BaseResponse<?>> getMainPage(
            @PathVariable("name") String name,
            @RequestParam("lat") Double lat,
            @RequestParam("lng") Double lng) {

        if (lat == null || lng == null) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(false, 400, "위치 값 입력 안됨!!!"));
        }

        try {
            MainPageResponseDto mainPageInfo = pinService.getMainPageInfo(name, lat, lng);
            BaseResponse<MainPageResponseDto> response = new BaseResponse<>("성공적으로 불러옴~", mainPageInfo);
            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(new BaseResponse<>(false, 404, e.getMessage()));
        }
    }

    /**
     * AI 기반 사용자 맞춤형 Top 5 카페 추천 API
     */
    @GetMapping("/mainpage/{name}/top5")
    public ResponseEntity<BaseResponse<Top5CafeResponseDto>> getTop5Cafes(
            @PathVariable("name") String name,
            @RequestParam("lat") Double lat,
            @RequestParam("lng") Double lng) {

        if (lat == null || lng == null) {
            return ResponseEntity.badRequest().body(new BaseResponse(false, 400, "위치 값 입력 안됨!!!"));
        }

        try {
            Top5CafeResponseDto top5Cafes = pinService.getTop5Cafes(name, lat, lng);
            BaseResponse<Top5CafeResponseDto> response = new BaseResponse<>("AI 추천 top 5~", top5Cafes);
            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(new BaseResponse<>(false, 404, e.getMessage()));
        }
    }

    @GetMapping("/mainpage/cafe/{pin_name}")
    public ResponseEntity<BaseResponse<?>> getCafeDetail(@PathVariable("pin_name") String pinName) {

        if (pinName == null || pinName.isBlank()) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(false, 400, "위치 값 입력 안됨!!!"));
        }

        try {
            CafeDetailResponseDto cafeDetailDto = pinService.getCafeDetail(pinName);
            BaseResponse<CafeDetailResponseDto> response = new BaseResponse<>("성공적으로 불러옴~", cafeDetailDto);
            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(new BaseResponse<>(false, 404, e.getMessage()));
        }
    }

    @GetMapping("/mainpage/{pin_name}/photos")
    public ResponseEntity<BaseResponse<?>> getCafePhotos(@PathVariable("pin_name") String pinName) {
        try {
            PhotoListResponseDto photoListDto = pinService.getPhotoUrls(pinName);
            BaseResponse<PhotoListResponseDto> response = new BaseResponse<>("성공적으로 불러옴~", photoListDto);
            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            if ("사진 없음".equals(e.getMessage())) {
                return ResponseEntity.badRequest().body(new BaseResponse<>(false, 400, e.getMessage()));
            } else {
                return ResponseEntity.status(404).body(new BaseResponse<>(false, 404, e.getMessage()));
            }
        }
    }
}
