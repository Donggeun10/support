package com.example.support.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChainBasicAuth(HttpSecurity http) throws Exception {
        http.csrf(csrfConfigurer ->  // POST 를 차단하는 기능임
                      csrfConfigurer.ignoringRequestMatchers(new AntPathRequestMatcher("/api/v1/announcement/**"))
            )
            .cors(corsConfigurer -> corsConfigurer.configurationSource(request -> {
                CorsConfiguration corsConfiguration = new CorsConfiguration();
                corsConfiguration.setAllowCredentials(true);
                corsConfiguration.addAllowedOrigin("http://localhost:8080");
                corsConfiguration.addAllowedHeader("*");
                corsConfiguration.addAllowedMethod("*");
                return corsConfiguration;
            }))
            .authorizeHttpRequests(
                config -> config.requestMatchers("/", "/healthz").permitAll()
                    .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/api-docs", "/swagger-ui.html","/actuator/**").permitAll()
                    .requestMatchers("/api/v1/announcement/**").authenticated()
                    .requestMatchers("/api/v1/announcements/**").authenticated()
                    .anyRequest().denyAll())
            .httpBasic(
                httpBasicConfigurer -> httpBasicConfigurer.realmName("Announcement API")
            );


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnProperty(name = "spring.h2.console.enabled",havingValue = "true")
    public WebSecurityCustomizer configureH2ConsoleEnable() {
        return web -> web.ignoring()
            .requestMatchers(PathRequest.toH2Console());
    }
}
