package wooribe.zarit.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "quest")
public class Quest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //01234까지 시작시 테이블 만들어져잇어야할듯
    @Column(name = "quest_id")
    private Long id;

    @Column(name = "quest_info")
    private String questInfo;

    private int reward = 0; // 기본값 0으로 설정
}