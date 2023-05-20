package com.example.progettocozzadelgaudio.configurations;

import com.example.progettocozzadelgaudio.authentication.JwtAuthenticationConverter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class SecurityConfiguration /*extends WebSecurityConfigurerAdapter*/ {

        @Bean
        public SecurityFilterChain configure(HttpSecurity http) throws Exception{
            http
                    .csrf((csrf)-> csrf.disable()).authorizeHttpRequests((auth)->  auth.anyRequest().authenticated() )
                    .oauth2ResourceServer().jwt().jwtAuthenticationConverter(new JwtAuthenticationConverter());
            http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }
        @Bean
        public CorsFilter corsFilter() {
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowCredentials(true);
            configuration.addAllowedOrigin("*");
            configuration.addAllowedOrigin("http://localhost:8080");
            configuration.addAllowedHeader("*");
            configuration.addAllowedMethod("OPTIONS");
            configuration.addAllowedMethod("GET");
            configuration.addAllowedMethod("POST");
            configuration.addAllowedMethod("PUT");
            configuration.addAllowedMethod("DELETE");
            source.registerCorsConfiguration("/**", configuration);
            return new CorsFilter(source);
        }

}
