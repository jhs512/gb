package com.gb.sapp.global.security;

import com.gb.sapp.global.app.AppConfig;
import com.gb.sapp.global.rsData.RsData;
import com.gb.sapp.standard.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .authorizeRequests(
                        authorizeRequests -> authorizeRequests
                                .requestMatchers(HttpMethod.GET, "/api/*/posts/{id:\\d+}", "/api/*/posts")
                                .permitAll()
                                .requestMatchers("/api/*/members/login", "/api/*/members/logout")
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .headers(
                        headers ->
                                headers.frameOptions(
                                        frameOptions ->
                                                frameOptions.sameOrigin()
                                )
                )
                .csrf(
                        csrf ->
                                csrf.disable()
                ).sessionManagement(
                        sessionManagement -> sessionManagement
                                .sessionCreationPolicy(
                                        SessionCreationPolicy.STATELESS
                                )
                ).exceptionHandling(
                        exceptionHandling -> exceptionHandling
                                .authenticationEntryPoint(
                                        (request, response, authException) -> {
                                            response.setContentType("application/json;charset=UTF-8");
                                            response.setStatus(403);
                                            response.setHeader("Access-Control-Allow-Credentials", "true"); // 쿠키 허용
                                            response.setHeader("Access-Control-Allow-Origin", AppConfig.getSiteFrontUrl()); // 특정 출처 허용
                                            response.getWriter().write(
                                                    Ut.json.toString(
                                                            RsData.of("403-1", request.getRequestURI() + ", " + authException.getLocalizedMessage())
                                                    )
                                            );
                                        }
                                )
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(
                        cors ->
                                cors.configurationSource(
                                        request -> {
                                            var corsConfig = new CorsConfiguration();
                                            corsConfig.setAllowCredentials(true);
                                            corsConfig.addAllowedOrigin(AppConfig.getSiteFrontUrl());
                                            corsConfig.addAllowedHeader("*");
                                            corsConfig.addAllowedMethod("*");

                                            return corsConfig;
                                        }
                                )
                );

        return http.build();
    }
}
