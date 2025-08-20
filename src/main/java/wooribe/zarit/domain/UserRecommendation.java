package wooribe.zarit.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_recommendation")
public class UserRecommendation {

    @Id
    @Column(name = "user_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // User의 id를 UserRecommendation의 id로 사용
    @JoinColumn(name = "user_id")
    private User user;

    // 추천된 Pin ID 목록을 저장
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "ranked_pin_ids", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "pin_id")
    @OrderColumn(name = "rank_idx")
    private List<Long> rankedPinIds;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public UserRecommendation(User user, List<Long> rankedPinIds) {
        this.user = user;
        this.rankedPinIds = rankedPinIds;
        this.updatedAt = LocalDateTime.now();
    }

    // 추천 결과를 업데이트하는 메서드
    public void update(List<Long> rankedPinIds) {
        this.rankedPinIds = rankedPinIds;
        this.updatedAt = LocalDateTime.now();
    }
}
