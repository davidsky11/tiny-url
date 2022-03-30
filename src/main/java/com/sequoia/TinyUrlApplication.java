package com.sequoia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * TinyUrlApplication
 *
 * @author KVLT
 * @date 2022-03-30.
 */
@SpringBootApplication(scanBasePackages = "com.sequoia")
public class TinyUrlApplication {

    public static void main(String[] args) {
        SpringApplication.run(TinyUrlApplication.class, args);
    }

}
