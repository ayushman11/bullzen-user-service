package com.bullzen.user;

import com.bullzen.user.entities.UserRole;
import com.bullzen.user.repository.UserRoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner seedRoles(UserRoleRepository userRoleRepository) {
		return args -> {
			List<String> roles = List.of("ROLE_ADMIN", "ROLE_TRADER");

			for(String role: roles) {
				if(!userRoleRepository.existsByName(role)) {
					UserRole newRole = UserRole.builder()
							.name(role)
							.build();
					userRoleRepository.save(newRole);
				}
			}
		};
	}

}
