package wooribe.zarit.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pin_environment {
    @Id
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pin_id")
    private Pin pin;

    @Column(nullable = false)
    private double noise;

    @Column(nullable = false)
    private double wifi;

    @Column(nullable = false)
    private double plugbar;

    @Column(nullable = false)
    private double congestion;

    // 평균 계산
    public void updateNoise(double newAverageNoise) {
        this.noise = newAverageNoise;
    }

    public void updateWifi(double newAverageWifi) {
        this.wifi = newAverageWifi;
    }

    public void updatePlugbar(double newAveragePlugbar) {
        this.plugbar = newAveragePlugbar;
    }

    // 혼잡도 업데이트
    public void updateCongestion(double newCongestion) {
        this.congestion = newCongestion;
    }
}
