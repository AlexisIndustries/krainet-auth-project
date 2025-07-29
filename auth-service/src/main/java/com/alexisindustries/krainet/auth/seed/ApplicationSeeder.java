package com.alexisindustries.krainet.auth.seed;

import com.alexisindustries.krainet.auth.model.Role;
import com.alexisindustries.krainet.auth.model.User;
import com.alexisindustries.krainet.auth.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/AlexisIndustries">AlexisIndustries</a>
 */
@Component
@Log4j2
public class ApplicationSeeder {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ApplicationSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener(ApplicationStartedEvent.class)
    private void seed() {
        User user = new User();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("password"));
        user.setFirstName("a");
        user.setLastName("b");
        user.setEmail("example@example.com");
        user.setRole(Role.USER);

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("superpassword"));
        admin.setFirstName("c");
        admin.setLastName("d");
        admin.setEmail("example2@example.com");
        admin.setRole(Role.ADMIN);

        if (userRepository.existsUserByUsername("user")) userRepository.save(user);
        log.info("Test User entity created.");
        if (userRepository.existsUserByUsername("admin")) userRepository.save(admin);
        log.info("Test ADMIN entity created.");
    }
}
