package com.tenpo.jarvis.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableConfigurationProperties
@SpringBootApplication(scanBasePackages = {"com.tenpo.jarvis.controller", "com.tenpo.jarvis.service", "com.tenpo.jarvis.config", "com.tenpo.jarvis.component",
        "com.tenpo.jarvis.entity", "com.tenpo.jarvis.repository"})
@EntityScan(basePackages = {"com.tenpo.jarvis.entity", "com.tenpo.jarvis.component", "com.tenpo.jarvis.config"})
@EnableJpaRepositories("com.tenpo.jarvis.repository")
public class JarvisApplication {

    public static void main(String[] args) {
        SpringApplication.run(JarvisApplication.class, args);
    }

}
