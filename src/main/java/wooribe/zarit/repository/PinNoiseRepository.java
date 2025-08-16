package wooribe.zarit.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import wooribe.zarit.domain.Pin_noise;

import java.util.List;

public interface PinNoiseRepository extends JpaRepository<Pin_noise, Long> {
    // pinName으로 모든 소음 데이터를 찾는 메서드
    List<Pin_noise> findByPinName(String name);
}
