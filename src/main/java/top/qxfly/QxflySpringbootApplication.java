package top.qxfly;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
@MapperScan("top.qxfly.mapper")
public class QxflySpringbootApplication {
    public static void main(String[] args) {
        SpringApplication.run(QxflySpringbootApplication.class, args);
    }

}
