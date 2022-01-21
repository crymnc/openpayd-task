package com.openpayd.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class OpenPaydTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenPaydTaskApplication.class, args);
	}

}
