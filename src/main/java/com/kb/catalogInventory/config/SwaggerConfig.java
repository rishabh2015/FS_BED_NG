package com.kb.catalogInventory.config;

import java.util.Arrays;
import java.util.HashSet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()                                  
				.apis(RequestHandlerSelectors.any())              
				.paths(PathSelectors.any()).build()
				.apiInfo(metaData())
				.produces(produceConsume())
				.consumes(produceConsume());


	}
	private ApiInfo metaData() {
		return new ApiInfoBuilder().title("KonnectBox Catalog Inventoty Service")
				.description("KonnectBox Catalog Inventory Service REST API Description")
				.termsOfServiceUrl("urn:tos")
				.contact(getContact())
				.license("Licence Url")
				.version("1.0")
				.build();
	}
	private Contact getContact() {
		return new Contact("Contact Name", "http://www.konnectbox.in", "operations@konnectbox.in");
	}
	private HashSet<String> produceConsume() {
		return new HashSet<String>(Arrays.asList("application/json","application/xml"));
	}
}
