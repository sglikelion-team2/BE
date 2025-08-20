package wooribe.zarit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wooribe.zarit.domain.UserRecommendation;

@Repository
public interface UserRecommendationRepository extends JpaRepository<UserRecommendation, Long> {
}
