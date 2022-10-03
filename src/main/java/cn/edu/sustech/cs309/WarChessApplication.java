package cn.edu.sustech.cs309;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class WarChessApplication {

    public static void main(String[] args) {
        SpringApplication.run(WarChessApplication.class, args);
    }

}
