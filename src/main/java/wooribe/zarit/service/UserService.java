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

        // 사용자가 없으면 신규 유저를 만들고 DB에 저장한다. 있으면 기존 유저를 가져온다.
        User user = optionalUser.orElseGet(() -> userRepository.save(request.toEntity()));

        // 201 (신규 계정) or 200 (기존 계정) 판단
        int code = userRepository.findByName(userName).isPresent() ? 200 : 201;
        String message = (code == 201) ? "계정 생성 성공" : "로그인 성공";

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
