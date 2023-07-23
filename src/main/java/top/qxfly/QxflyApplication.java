package top.qxfly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class QxflyApplication {

    public static void main(String[] args) {
        SpringApplication.run(QxflyApplication.class, args);
    }

}
