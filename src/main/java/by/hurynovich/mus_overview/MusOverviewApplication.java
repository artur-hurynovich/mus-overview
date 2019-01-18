package by.hurynovich.mus_overview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class MusOverviewApplication {

    public static void main(final String[] args) {
        SpringApplication.run(MusOverviewApplication.class, args);
    }

}

