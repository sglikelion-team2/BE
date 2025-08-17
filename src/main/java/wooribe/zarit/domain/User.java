package wooribe.zarit.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@DynamicInsert // default 값을 적용하기 위해 사용
@Table(name = "user") // DB 테이블 이름을 명시적으로 지정
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @Column(nullable = false)
    @ColumnDefault("'testuser'") // 기본값 설정
    private String name;

    @Column(name = "point")// 컬럼명을 명시적으로 지정
    @ColumnDefault("0")
    private int point;

    public void addPoint(int pointsToAdd) {
        if (pointsToAdd > 0) { // 양수만 더하도록 검증
            this.point += pointsToAdd;
        }
    }
}