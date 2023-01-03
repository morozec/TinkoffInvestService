package com.morozec.tinkoffservicemaven;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableConfigurationProperties
@EnableAsync
public class TinkoffServiceMavenApplication {

    public static void main(String[] args) {
        SpringApplication.run(TinkoffServiceMavenApplication.class, args);
    }

}
