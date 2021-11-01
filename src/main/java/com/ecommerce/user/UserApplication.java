package com.ecommerce.user;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class UserApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}
	@Bean
	public Docket api() {
	return new Docket(DocumentationType.SWAGGER_2)
	.select()
	.apis(RequestHandlerSelectors.basePackage("com.ecommerce.user"))
	.paths(PathSelectors.any())
	.build()
	.apiInfo(apiDeatils());
	}
	private ApiInfo apiDeatils()
	{
	return new ApiInfo(
	"User Microservice API",
<<<<<<< HEAD
	"User Microservice interacts with Item Microservice and order Microservice \r\n" +
	" \r\n" +
	"• User can signup or can do registration process \r\n" +
	"• User microservice provides forgot password fields \r\n" +
	"• User can update Email verifications \r\n" +
	"• User microservice validates the user details \r\n" ,
=======
	"User Microservice holds the security configuration  \r\n" +
	" \r\n" +
	"• User can signup or can do registration process \r\n" +
	"• User microservice provides reset password option \r\n" +
	"• User can login using username and password \r\n" +
	"• User microservice validates the user details and get user wallet amount \r\n" ,
>>>>>>> origin/Userswagger
	"1.0",
	"©Copyright Application",
	new springfox.documentation.service.Contact("Fassico", "https://Fassico.com", "Fassico@gmail.com"),
	"API License",
	"https://Fassico.com",
	Collections.emptyList());
	}
}
