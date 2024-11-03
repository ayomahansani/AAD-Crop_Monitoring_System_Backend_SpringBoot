package lk.ijse.CropMonitoringSystem_Backend;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
@EnableMethodSecurity
public class CropMonitoringSystemBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CropMonitoringSystemBackendApplication.class, args);
	}

	//create modelmapper object
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
