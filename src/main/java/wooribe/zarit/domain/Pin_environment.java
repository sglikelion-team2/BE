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
    private double congestion;

}
