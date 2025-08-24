package wooribe.zarit.domain;

import jakarta.persistence.*;
import lombok.*;
import wooribe.zarit.domain.User;
import wooribe.zarit.domain.enums.Atmos;
import wooribe.zarit.domain.enums.Facility;
import wooribe.zarit.domain.enums.Purpose;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)

@Table(name = "user_preference")
public class User_preference {

    @Id
    @Column(name = "user_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // User의 id를 UserPreference의 id로 사용
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.ORDINAL) // Enum의 순서(0, 1, 2...)를 DB에 저장
    @Column(nullable = false)
    private Purpose purpose = Purpose.STUDY; // 기본값 '공부(0)'

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private Atmos atmos = Atmos.QUIET; // 기본값 '조용(0)'

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private Facility facility = Facility.POWER; // 기본값 '콘센트(0)'

    // 선호도 정보를 업데이트하는 메서드 추가
    public void update(Purpose purpose, Atmos atmos, Facility facility) {
        if (purpose != null) this.purpose = purpose;
        if (atmos != null) this.atmos = atmos;
        this.facility = (facility != null) ? facility : Facility.NONE;
    }

}
