package wooribe.zarit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling // 스케줄링 기능 활성화
@SpringBootApplication
public class ZaritApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZaritApplication.class, args);
	}

}
