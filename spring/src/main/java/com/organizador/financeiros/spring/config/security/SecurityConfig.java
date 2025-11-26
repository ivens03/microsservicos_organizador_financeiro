package com.organizador.financeiros.spring.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Bean responsavel por fazer a criptografia usando o BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // =================================================================//
    //  BEAN (Usuário Fixo para Testes)
    //  Cria usuários fixos em memória para testes de API.
    //  PasswordEncoder O encoder BCrypt injetado.
    // =================================================================//
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {

        // Cria um usuário "admin" com senha "admin123"
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder.encode("admin123"))
                // .roles("ADMIN", "USER") // Define os papéis (ROLES)
                .build();

        // Você pode adicionar quantos usuários de teste quiser
        UserDetails user = User.withUsername("user")
                .password(passwordEncoder.encode("user123"))
                // .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }
    // =================================================================//

    /**
     * Este é o Bean que configura todas as regras de segurança HTTP.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/ususarios/cadastrar",
                                         "/vendedor/cadastrar",
                                         "/swagger-ui/**",
                                         "/v3/api-docs/**").permitAll()
                        .anyRequest().permitAll()/*.authenticated()*/
                ).httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
