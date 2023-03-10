package com.kb.catalogInventory.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.kb.java.utils.ServiceIntercepter;


@Component
public class ServiceInterceptorAppConfig  implements WebMvcConfigurer {
	   @Autowired
	   ServiceIntercepter productServiceInterceptor;

	   @Override
	   public void addInterceptors(InterceptorRegistry registry) {
	      registry.addInterceptor(productServiceInterceptor);
	   }
	}