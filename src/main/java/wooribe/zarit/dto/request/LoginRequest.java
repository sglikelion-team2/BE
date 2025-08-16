package wooribe.zarit.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wooribe.zarit.domain.User;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    private String name;

    public User toEntity() {
        return User.builder()
                .name(name)
                .build();
    }

}
