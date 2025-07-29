package com.alexisindustries.krainet.auth.config;

import com.alexisindustries.krainet.auth.security.filter.JwtFilter;
import com.alexisindustries.krainet.auth.security.handler.CustomAccessDeniedHandler;
import com.alexisindustries.krainet.auth.security.handler.CustomLogoutHandler;
import com.alexisindustries.krainet.auth.service.JwtService;
import com.alexisindustries.krainet.auth.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * @author <a href="https://github.com/AlexisIndustries">AlexisIndustries</a>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomLogoutHandler customLogoutHandler;
    private final JwtService jwtService;

    public SecurityConfiguration(
            CustomAccessDeniedHandler accessDeniedHandler,
            CustomLogoutHandler customLogoutHandler,
            JwtService jwtService
    ) {
        this.accessDeniedHandler = accessDeniedHandler;
        this.customLogoutHandler = customLogoutHandler;
        this.jwtService = jwtService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           UserService userService,
                                           JwtFilter jwtFilter) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/api/auth/login/**", "/api/auth/register/**", "/api/auth/refresh_token/**", "/")
                            .permitAll();
                    auth.requestMatchers("/api/user/**").authenticated();
                    auth.anyRequest().permitAll();
                })
                .userDetailsService(userService)
                .exceptionHandling(e -> {
                    e.accessDeniedHandler(accessDeniedHandler);
                    e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
                })
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(log -> {
                    log.logoutUrl("/logout");
                    log.addLogoutHandler(customLogoutHandler);
                    log.logoutSuccessHandler((request, response, authentication) ->
                            SecurityContextHolder.clearContext());
                });

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public JwtFilter jwtFilter(UserService userService) {
        return new JwtFilter(jwtService, userService);
    }
}
