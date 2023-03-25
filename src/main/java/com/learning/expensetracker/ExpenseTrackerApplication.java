package com.learning.expensetracker;

import com.learning.expensetracker.filters.AuthFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@SpringBootApplication
public class ExpenseTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpenseTrackerApplication.class, args);
	}


	@Bean
	public FilterRegistrationBean<AuthFilter> filterRegistrationBean(){
		FilterRegistrationBean<AuthFilter> filterRegistrationBean=new FilterRegistrationBean<>();

		filterRegistrationBean.setFilter(new AuthFilter());
		filterRegistrationBean.addUrlPatterns("/api/categories/*");
		return filterRegistrationBean;
	}

	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter(){
		FilterRegistrationBean<CorsFilter> filterRegistrationBean=new FilterRegistrationBean<>();
		UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
		CorsConfiguration configuration=new CorsConfiguration();
		configuration.addAllowedOrigin("*");
		configuration.addAllowedHeader("*");
		source.registerCorsConfiguration("/**",configuration);

		filterRegistrationBean.setFilter(new CorsFilter(source));
		filterRegistrationBean.setOrder(0);

		return filterRegistrationBean;


	}


}
