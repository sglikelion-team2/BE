package wooribe.zarit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wooribe.zarit.domain.Photo;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
}
