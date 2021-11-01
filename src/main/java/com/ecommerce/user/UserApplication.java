package com.ecommerce.user;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class UserApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}
	@Bean
    public Docket swaggerConfig() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("com.ecommerce")).build().apiInfo(apiDetails());
    }
    
    public ApiInfo apiDetails() {
        return new ApiInfo("User-Service API", "This is an api of authorization in Ecommerce Application", "1.0",
                "Authorization Api", new Contact("Neeraj", null, "xyz@gmail.com"), "license", "http://localhost:9092",
                Collections.emptyList());
    }

}
