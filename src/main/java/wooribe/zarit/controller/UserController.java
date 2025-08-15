package wooribe.zarit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooribe.zarit.dto.request.LoginRequest;
import wooribe.zarit.dto.request.UserPreferenceRequest;
import wooribe.zarit.dto.response.ApiResponse;
import wooribe.zarit.dto.response.UserPreferenceResponse;
import wooribe.zarit.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/login")
public class UserController {
    private final UserService userService;

    // 로그인
    @PostMapping
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest request) {
        ApiResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }

    // 선호도
    @PostMapping("/{name}")
    public ResponseEntity<ApiResponse> preference(@RequestBody UserPreferenceRequest request) {
        UserPreferenceResponse response = userService.updatePrefer(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(true, 200, "계정 선호도 생성 완료", response));
    }
}
