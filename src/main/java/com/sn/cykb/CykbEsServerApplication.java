package com.sn.cykb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author songning
 */
@EnableFeignClients(basePackages = {"com.sn.cykb.feign"})
@SpringBootApplication
public class CykbEsServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CykbEsServerApplication.class, args);
    }

}
