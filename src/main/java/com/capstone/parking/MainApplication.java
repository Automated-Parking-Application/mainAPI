package com.capstone.parking;

import java.util.Arrays;

import com.capstone.parking.filters.AdminFilter;
import com.capstone.parking.filters.JwtFilter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class MainApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean<JwtFilter> loggingFilter() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean<>();

		registrationBean.setFilter(new JwtFilter());
		// registrationBean.addUrlPatterns("/user/*");
		registrationBean.addUrlPatterns("/parking-space/*");
		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean<JwtFilter> adminFilter() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean<>();

		registrationBean.setFilter(new AdminFilter());
		registrationBean.addUrlPatterns("/admin/*");
		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.setAllowedMethods(Arrays.asList("POST", "OPTIONS", "GET", "DELETE", "PUT", "OPTION"));
		config.setAllowedHeaders(Arrays.asList("X-Requested-With", "Origin", "Content-Type", "Accept", "Authorization"));
		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}