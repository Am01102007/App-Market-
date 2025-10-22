package co.edu.uniquindio.ProyectoFinalp3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EnableJpaAuditing
@EntityScan(basePackages = "co.edu.uniquindio.ProyectoFinalp3.models")

public class ProyectoFinalp3Application {

	public static void main(String[] args) {
		SpringApplication.run(ProyectoFinalp3Application.class, args);

	}

}
