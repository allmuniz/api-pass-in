package com.project.passin;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Pass-in",
				description = "API responsavel pela gestão de eventos",
				version = "1"
		)
)
public class PassInApplication {

	public static void main(String[] args) {
		SpringApplication.run(PassInApplication.class, args);
	}

}
