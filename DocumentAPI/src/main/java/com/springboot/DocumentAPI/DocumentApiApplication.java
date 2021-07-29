package com.springboot.DocumentAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.springboot.DocumentAPI.Config.AWSS3Client;

@SpringBootApplication
public class DocumentApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocumentApiApplication.class, args);
	}

}
