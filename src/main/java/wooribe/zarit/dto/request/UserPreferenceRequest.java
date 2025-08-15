package wooribe.zarit.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wooribe.zarit.domain.User;
import wooribe.zarit.domain.User_preference;
import wooribe.zarit.domain.enums.Atmos;
import wooribe.zarit.domain.enums.Facility;
import wooribe.zarit.domain.enums.Purpose;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferenceRequest {
    private String name;
    private Purpose purpose;
    private Atmos atmos;
    private Facility facility;

    public User_preference toEntity(User user) {
        return User_preference.builder()
                .user(user)
                .purpose(purpose)
                .atmos(atmos)
                .facility(facility)
                .build();
    }
}
