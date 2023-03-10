package com.kb.catalogInventory.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		 http.csrf().disable();
	        http.headers().httpStrictTransportSecurity().disable();
	        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

	        // Authorize sub-folders permissions
	        http.antMatcher("/**").authorizeRequests().anyRequest().permitAll();	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/locale/**").antMatchers("/test/**").antMatchers("/actuator/**")
		.antMatchers("/authenticate/**");
//		web.ignoring().antMatchers("/locale/**").antMatchers("/test/**").antMatchers("/actuator/**").antMatchers("/authenticate/**").antMatchers("/signUp/**")
//				.antMatchers("/h2-ui/**").antMatchers("/v2/api-docs/**").antMatchers("/social/**").antMatchers("/resetPassword/**");
	}
}
