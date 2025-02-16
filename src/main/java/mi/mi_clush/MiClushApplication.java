package mi.mi_clush;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MiClushApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiClushApplication.class, args);
    }

}
