package wooribe.zarit.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import wooribe.zarit.domain.User;
import wooribe.zarit.domain.User_preference;
import wooribe.zarit.domain.enums.Atmos;
import wooribe.zarit.domain.enums.Facility;
import wooribe.zarit.domain.enums.Purpose;

@Getter
@Builder
@AllArgsConstructor
public class UserPreferenceResponse {
    private final String name;
    private final Purpose purpose;
    private final Atmos atmos;
    private final Facility facility;

    public static UserPreferenceResponse of(User user, User_preference prefer) {
        return UserPreferenceResponse.builder()
                .name(user.getName())
                .purpose(prefer.getPurpose())
                .atmos(prefer.getAtmos())
                .facility(prefer.getFacility())
                .build();
    }

}
