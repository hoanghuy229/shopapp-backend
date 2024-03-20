package com.spring.shopappbackend.configuration;

import com.spring.shopappbackend.component.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {
    private final JwtTokenFilter jwtTokenFilter;



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(AbstractHttpConfigurer::disable)
            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(requests ->{
                requests
                        .requestMatchers(HttpMethod.GET,Endpoints.PUBLIC_GET_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.POST,Endpoints.PUBLIC_POST_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.GET,Endpoints.ADMIN_USER_GET_ENDPOINTS).hasAnyRole("ADMIN","USER")
                        .requestMatchers(HttpMethod.POST,Endpoints.ADMIN_USER_POST_ENDPOINTS).hasAnyRole("ADMIN","USER")
                        .requestMatchers(HttpMethod.POST,Endpoints.ADMIN_POST_ENDPOINTS).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,Endpoints.ADMIN_PUT_ENDPOINTS).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,Endpoints.ADMIN_DELETE_ENDPOINTS).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,Endpoints.USER_POST_ENDPOINTS).hasRole("USER")
                        .requestMatchers(HttpMethod.PUT,Endpoints.USER_PUT_ENDPOINTS).hasRole("USER")
                        .anyRequest().authenticated();
            });
        http.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
            @Override
            public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("*"));
                configuration.setAllowedMethods(Arrays.asList("GET","POST","PATCH","PUT","DELETE","OPTION"));
                configuration.setAllowedHeaders(Arrays.asList("authorization","content-type","x-auth-token"));
                configuration.setExposedHeaders(List.of("x-auth-token"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**",configuration);
                httpSecurityCorsConfigurer.configurationSource(source);
            }
        });
    return http.build();
    }
}
