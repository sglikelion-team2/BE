package wooribe.zarit.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wooribe.zarit.dto.request.NoiseRequest;
import wooribe.zarit.dto.request.PlugbarRequest;
import wooribe.zarit.dto.request.WifiRequest;
import wooribe.zarit.dto.response.ApiResponse;
import wooribe.zarit.dto.response.QuestResponse;
import wooribe.zarit.service.QuestService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/quest")
public class QuestController {
    private final QuestService questService;

    @PostMapping("/{name}/noise")
    public ResponseEntity<ApiResponse> noiseQuest(@PathVariable("name") String name, @RequestBody @Valid NoiseRequest request) {
        QuestResponse response = questService.completeNoiseQuest(name, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(true, 200, "소음 퀘스트 완료", response));
    }

    @PostMapping("/{name}/wifi")
    public ResponseEntity<ApiResponse> wifiQuest(@PathVariable("name") String name, @RequestBody @Valid WifiRequest request) {
        QuestResponse response = questService.completeWifiQuest(name, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(true, 200, "와이파이 퀘스트 완료", response));
    }

    @PostMapping("/{name}/plugbar")
    public ResponseEntity<ApiResponse> plugbarQuest(@PathVariable("name") String name, @RequestBody @Valid PlugbarRequest request) {
        QuestResponse response = questService.completePlugbarQuest(name, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(true, 200, "콘센트 퀘스트 완료", response));
    }

    @PostMapping("/{name}/atmos")
    public ResponseEntity<ApiResponse> completePhotoQuest(
            @PathVariable("name") String name,
            @RequestParam("pin_name") String pinName,
            @RequestPart("photo") MultipartFile file) {

        // 서비스 메서드 호출
        QuestResponse questResponse = questService.completePhotoQuest(name, pinName, file);

        // 응답 반환
        ApiResponse apiResponse = new ApiResponse(true, 200, "사진 퀘스트 완료", questResponse);
        return ResponseEntity.ok(apiResponse);
    }
}
