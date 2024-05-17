package com.mailer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@PropertySource("file:C:/Properties/application.properties")
public class Application {
 public static void main(String[] args) {
	 SpringApplication.run(Application.class,args);
 }
}
