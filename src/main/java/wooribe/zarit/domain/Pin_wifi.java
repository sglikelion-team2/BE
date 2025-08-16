package wooribe.zarit.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "pin_wifi")
public class Pin_wifi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 독립적인 기본 키(PK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pin_id") // 외래 키(FK) 컬럼 지정
    private Pin pin;

    private int wifi;

    public Pin_wifi(Pin pin, int wifi) {
        this.pin = pin;
        this.wifi = wifi;
    }
}