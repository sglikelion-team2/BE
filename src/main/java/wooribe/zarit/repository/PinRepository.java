package wooribe.zarit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import wooribe.zarit.domain.Pin;

import java.util.List;
import java.util.Optional;

@Repository
public interface PinRepository extends JpaRepository<Pin, Long> {
    Optional<Pin> findByName(String name);

    List<Pin> findTop5ByOrderByIdAsc();

    // 위도, 경도 및 반경(km)을 기준으로 근처 핀을 찾는 쿼리
    @Query(value = "SELECT p.*, " +
                   "(6371 * acos( " +
                   "cos(radians(:userLat)) * cos(radians(p.lat)) * cos(radians(p.lng) - radians(:userLng)) " +
                   "+ sin(radians(:userLat)) * sin(radians(p.lat)) " +
                   ") " +
                   ") AS distance " +
                   "FROM pin p " +
                   "HAVING distance < :distanceInKm " +
                   "ORDER BY distance", nativeQuery = true)
    List<Pin> findPinsWithinDistance(double userLat, double userLng, double distanceInKm);
}
