package wooribe.zarit.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import wooribe.zarit.domain.User;

@Getter
@Builder
@AllArgsConstructor
public class UserResponse {
    private final String name;


    public static UserResponse of(User user) {
        return UserResponse.builder()
                .name(user.getName())
                .build();
    }
}
