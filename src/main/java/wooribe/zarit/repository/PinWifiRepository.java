package wooribe.zarit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wooribe.zarit.domain.Pin_wifi;

import java.util.List;

public interface PinWifiRepository extends JpaRepository<Pin_wifi, Long> {
    // pinName으로 모든 와이파이 데이터를 찾는 메서드
    List<Pin_wifi> findByPinName(String name);
}

