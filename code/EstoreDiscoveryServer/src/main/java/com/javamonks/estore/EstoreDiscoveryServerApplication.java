package com.javamonks.estore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EstoreDiscoveryServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EstoreDiscoveryServerApplication.class, args);
	}

}
