package wooribe.zarit.te;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component // Spring이 이 클래스를 찾아서 실행할 수 있도록 등록합니다.
public class DBConnectionTester implements CommandLineRunner {

    // DB에 쉽게 쿼리를 날릴 수 있는 도구를 주입받습니다.
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("==========================================");
        System.out.println(">> DB 연결 테스트를 시작합니다...");

        try {
            // 아주 간단한 쿼리를 실행해서 DB로부터 응답이 오는지 확인합니다.
            jdbcTemplate.execute("SELECT 1");
            System.out.println(">> ✅ DB 연결에 성공했습니다!");
        } catch (Exception e) {
            System.err.println(">> ❌ DB 연결에 실패했습니다: " + e.getMessage());
        }

        System.out.println("==========================================");
    }
}