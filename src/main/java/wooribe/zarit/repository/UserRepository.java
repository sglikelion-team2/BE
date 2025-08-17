package wooribe.zarit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wooribe.zarit.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);
}
