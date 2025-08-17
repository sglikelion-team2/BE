package wooribe.zarit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wooribe.zarit.domain.Pin_plugbar;

import java.util.List;

public interface PinPlugbarRepository extends JpaRepository<Pin_plugbar, Long> {
    // pinName으로 모든 콘센트 데이터를 찾는 메서드
    List<Pin_plugbar> findByPinName(String name);
}

