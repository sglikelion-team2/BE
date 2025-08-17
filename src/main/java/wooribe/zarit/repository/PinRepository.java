package wooribe.zarit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wooribe.zarit.domain.Pin;

import java.util.List; // List import 추가
import java.util.Optional;

public interface PinRepository extends JpaRepository<Pin, Long> {
    Optional<Pin> findByName(String name);

    // ID를 기준으로 오름차순 정렬하여 상위 5개만 조회하는 메서드
    List<Pin> findTop5ByOrderByIdAsc();
}

