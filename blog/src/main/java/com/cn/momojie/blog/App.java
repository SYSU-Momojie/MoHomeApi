package com.cn.momojie.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:/applicationContext.xml")
public class App {

	public static void main(String args[]) {
		SpringApplication.run(App.class, args);
	}
}
