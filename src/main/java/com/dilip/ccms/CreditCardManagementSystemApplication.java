package com.dilip.ccms;

import com.dilip.ccms.address.CityRepository;
import com.dilip.ccms.address.DataSeeder;
import com.dilip.ccms.address.StateRepository;
import com.dilip.ccms.role.Role;
import com.dilip.ccms.role.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class CreditCardManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(CreditCardManagementSystemApplication.class, args);
	}
	@Bean
	public CommandLineRunner runner(RoleRepository roleRepository) {
		return args -> {
			if (roleRepository.findByName("USER").isEmpty()) {
				roleRepository.save(Role.builder().name("USER").build());
			}
		};
	}
}
