package com.usuario;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class ApiUsuarioCrudApplication {

	public static void main(String[] args) {

		SpringApplication.run(ApiUsuarioCrudApplication.class, args);
	}
	@Contract(value = " -> new", pure = true)
	@Bean
	public static @NotNull WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(@NotNull CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
						.allowedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Headers",
								"Access-Control-Allow-Methods", "Accept", "Authorization", "Content-Type", "Method",
								"Origin", "X-Forwarded-For", "X-Real-IP", "text/xml; charset=ISO-8859-1");
			}
		};
	}

}
