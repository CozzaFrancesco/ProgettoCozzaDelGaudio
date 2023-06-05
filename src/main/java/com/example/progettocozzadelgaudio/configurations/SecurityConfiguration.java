package com.example.progettocozzadelgaudio.configurations;

import com.example.progettocozzadelgaudio.authentication.JwtAuthenticationConverter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable()).authorizeHttpRequests((auth) ->
                        auth.requestMatchers("/registrazione/**").permitAll()
                                .requestMatchers(HttpMethod.OPTIONS).permitAll()
                                /*    .requestMatchers("/farmacia/magazzino").hasAuthority("farmacia")
                                    .requestMatchers("/carerllo/**").hasAuthority("farmacia")
                                    .requestMatchers("/cliente/**").hasAuthority("cliente")
                                    .requestMatchers("/gestore/**").hasAuthority("admin")
                                    .requestMatchers(HttpMethod.GET,"/prodotti/**").hasAuthority("gestore")
                                    .requestMatchers(HttpMethod.GET,"/prodotti/**").hasAuthority("farmacia")
                                    .requestMatchers(HttpMethod.PUT,"/prodotti/**").hasAuthority("gestore")
                                    .requestMatchers(HttpMethod.POST,"/prodotti/**").hasAuthority("gestore")
                                    .requestMatchers("/visite/**").hasAuthority("gestore")
                                    .requestMatchers(HttpMethod.PUT,"/farmacie/**").hasAuthority("gestore")
                                    .requestMatchers(HttpMethod.POST,"/farmacie/**").hasAuthority("cliente")
                                    .requestMatchers(HttpMethod.GET,"/farmacie/**").hasAuthority("cliente")
                                    .requestMatchers(HttpMethod.GET,"/farmacie","/farmacie/{citta}").hasAuthority("gestore")
                                  */
                                .anyRequest().authenticated())

                .oauth2ResourceServer().jwt().jwtAuthenticationConverter(new JwtAuthenticationConverter());
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }


    /*
        @Bean
        public CorsFilter corsFilter() {
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowCredentials(true);
            //configuration.addAllowedOrigin("*");
            configuration.addAllowedOriginPattern("*");
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

     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}