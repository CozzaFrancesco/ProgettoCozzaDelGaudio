package com.example.progettocozzadelgaudio.configurations;

import com.progetto.biglietteria.authentication.JwtAuthenticationConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
                    .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .antMatchers("/stazione/**").permitAll()
                    .antMatchers("/tratta/**").permitAll()
                    .antMatchers("/treno/**").permitAll()
                    .antMatchers("/orario/**").permitAll()
                    .antMatchers("/auth/biglietto/**").permitAll()
                    .antMatchers("/cliente/**").permitAll()
                    .anyRequest().authenticated().and().oauth2ResourceServer().jwt().jwtAuthenticationConverter(authenticationConverter());
        }

        @Bean
        public Converter<Jwt, AbstractAuthenticationToken> authenticationConverter() {
            return new JwtAuthenticationConverter();
        }

        @Bean
        public CorsFilter corsFilter() {
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowCredentials(true);
            configuration.addAllowedOrigin("http://localhost:4200");
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