package wooribe.zarit.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wooribe.zarit.domain.User;
import wooribe.zarit.domain.User_preference;
import wooribe.zarit.dto.request.UserPreferenceRequest;
import wooribe.zarit.dto.request.LoginRequest;
import wooribe.zarit.dto.response.ApiResponse;
import wooribe.zarit.dto.response.UserPreferenceResponse;
import wooribe.zarit.dto.response.UserResponse;
import wooribe.zarit.repository.UserPreferenceRepository;
import wooribe.zarit.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserPreferenceRepository userPreferenceRepository;

    // 로그인
    @Transactional
    public ApiResponse login(LoginRequest request) {
        if (request == null || request.getName() == null || request.getName().isBlank()) {
            // 실패 응답을 위한 ApiResponse를 직접 만들어서 반환
            throw new IllegalArgumentException("이름이 누락되었습니다.");
        }

        String userName = request.getName();

        Optional<User> optionalUser = userRepository.findByName(userName);
        User user;
        int code;
        String message;

        if (optionalUser.isPresent()) {
            // 이미 있는 유저
            user = optionalUser.get();
            code = 200;
            message = "로그인 성공";
        } else {
            // 새로운 유저
            user = userRepository.save(request.toEntity());
            code = 201;
            message = "계정 생성 성공";
        }

        return new ApiResponse(true, code, message, UserResponse.of(user));

    }

    // 선호도 선택
    @Transactional
    public UserPreferenceResponse updatePrefer (UserPreferenceRequest request) {
        // 요청의 name 이용해 User 객체 찾기
        User user = userRepository.findByName(request.getName())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 해당 User의 선호도 정보가 있는지 확인
        Optional<User_preference> optionalPreference = userPreferenceRepository.findById(user.getUser_id());

        User_preference userPreference;
        if (optionalPreference.isPresent()) {
            // 3. 기존 정보가 있으면, 엔티티를 가져와서 값만 수정
            userPreference = optionalPreference.get();
            userPreference.update(request.getPurpose(), request.getAtmos(), request.getFacility());

        } else {
            // 4. 기존 정보가 없으면, 새로 만들어서 저장
            userPreference = request.toEntity(user);
            userPreferenceRepository.save(userPreference);
        }

        // 응답 DTO로 변환해 반환
        return UserPreferenceResponse.of(user, userPreference);

    }
}
