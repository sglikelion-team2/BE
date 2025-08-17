package wooribe.zarit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wooribe.zarit.domain.Pin;
import wooribe.zarit.domain.Pin_environment;

import java.util.Optional;

public interface PinEnvironmentRepository extends JpaRepository<Pin_environment, Long> {

}
