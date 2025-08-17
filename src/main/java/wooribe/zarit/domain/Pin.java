package wooribe.zarit.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "pin")
public class Pin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double lat;

    @Column(nullable = false)
    private Double lng;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String pin_info;

    @Column(nullable = false)
    private Boolean is_partnered;

    @Column(nullable = false)
    private String seat;

    @Column(nullable = false)
    private LocalTime open_hour;

    @Column(nullable = false)
    private LocalTime close_hour;

    @Column(columnDefinition = "DOUBLE DEFAULT 3.0")
    private Double rate = 3.0;

    @OneToOne(mappedBy = "pin")
    private Pin_environment pin_environment;

    @OneToMany(mappedBy = "pin")
    private List<Pin_noise> pin_noises = new ArrayList<>();

    @OneToMany(mappedBy = "pin")
    private List<Pin_photo> pin_photos = new ArrayList<>();

    @OneToMany(mappedBy = "pin")
    private List<Pin_plugbar> pin_plugbars = new ArrayList<>();

    @OneToMany(mappedBy = "pin")
    private List<Pin_wifi> pin_wifis = new ArrayList<>();

    @OneToMany(mappedBy = "pin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Photo> photos = new ArrayList<>();

    // 네가 직접 작성한 생성자 (빠진 필드 추가)
    public Pin(String name, String address, String category, String pin_info, Boolean is_partnered, String seat, Double lat, Double lng, LocalTime open_hour, LocalTime close_hour, Double rate) {

        this.name = name;
        this.address = address;
        this.category = category;
        this.pin_info = pin_info;
        this.is_partnered = is_partnered;
        this.seat = seat;
        this.lat = lat;
        this.lng = lng;
        this.open_hour = open_hour;
        this.close_hour = close_hour;
        this.rate = rate;


        // seat의 기본값 설정 로직
        if (this.seat == null) {
            this.seat = "{\"1\":4,\"2\":5,\"4\":6,\"6\":2}"; // 이 부분을 원하는 기본값으로 설정
        }

    }


}
