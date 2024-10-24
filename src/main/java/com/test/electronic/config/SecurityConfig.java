package com.test.electronic.config;

import com.test.electronic.filter.JwtAuthorizationFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
    @RequiredArgsConstructor
    @Slf4j
    public class SecurityConfig {

        @Bean
        public static BCryptPasswordEncoder passwordEncoder(){
            return new BCryptPasswordEncoder();
        }
        private final CustomUserDetailsService userDetailsService;
        private final JwtAuthorizationFilter jwtAuthorizationFilter;

        @Bean
        public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder passwordEncoder)
                throws Exception {
            AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
            authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
            return authenticationManagerBuilder.build();
        }


        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.csrf(csrf->csrf.disable())
                    .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(
                            authorize -> authorize
                                    .requestMatchers(permitAllUrls).permitAll()
                                    .requestMatchers("/payments/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                                    .requestMatchers("/categories/**").hasAnyAuthority("ROLE_ADMIN")
                                    .requestMatchers(HttpMethod.GET, "/categories").hasAnyAuthority("ROLE_USER")
                                    .requestMatchers("/orders/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                                    .requestMatchers("/products/**").hasAnyAuthority( "ROLE_ADMIN")
                                    .requestMatchers(HttpMethod.GET, "/products").hasAnyAuthority("ROLE_USER")
                                    .requestMatchers("/promotions/**").hasAnyAuthority( "ROLE_ADMIN")
                                    .requestMatchers(HttpMethod.GET, "/promotions").hasAnyAuthority("ROLE_USER")



                                    .anyRequest().authenticated()
                    ).exceptionHandling(exceptionHandling -> exceptionHandling
                            .authenticationEntryPoint((request, response, authException) ->
                                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED)
                            )
                            .accessDeniedHandler((request, response, accessDeniedException) ->
                                    response.setStatus(HttpServletResponse.SC_FORBIDDEN)
                            )
                    );
            return http.build();

        }

        static String[] permitAllUrls = {
                "/api/v1/auth/**",
                "/v2/api-docs",
                "/v3/api-docs",
                "/v3/api-docs/**",
                "/swagger-resources",
                "/swagger-resources/**",
                "/configuration/ui",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/auth/**",
                "/users/**"

        };

        static String[] adminUrls = {
                "/categories/new"


        };

        static String[] userUrls = {
                "/categories/new",
        };

        static String[] anyAuthUrls = {
                "/controller/any"
        };

    }
