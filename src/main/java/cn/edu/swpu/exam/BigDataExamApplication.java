package cn.edu.swpu.exam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BigDataExamApplication {

    public static void main(String[] args) {
        SpringApplication.run(BigDataExamApplication.class, args);
    }

}
