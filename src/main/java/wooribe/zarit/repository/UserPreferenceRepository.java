package wooribe.zarit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wooribe.zarit.domain.User_preference;

public interface UserPreferenceRepository extends JpaRepository<User_preference, Long> {
}
