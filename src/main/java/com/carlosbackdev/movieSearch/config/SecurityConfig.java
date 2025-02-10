package com.carlosbackdev.movieSearch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .cors().and() // Habilitar CORS
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll() // Permitir acceso sin autenticación
                .requestMatchers("/api/lists/**").permitAll() 
                .requestMatchers("/api/comments/**").permitAll() 
                    .requestMatchers(HttpMethod.DELETE,"/api/delete/comment").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/lists/delete").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/lists/delete/movie").authenticated()
                .requestMatchers("/api/text/process").permitAll()
                .anyRequest().authenticated() // Proteger todos los demás endpoints
            );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configuración global de CORS
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Aplicar a todos los endpoints
                    .allowedOrigins("http://localhost:4200") // Permitir solicitudes desde este origen
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos permitidos
                    .allowedHeaders("*") // Permitir todos los encabezados
                    .allowCredentials(true); // Permitir credenciales (cookies, tokens)
            }
        };
    }
}