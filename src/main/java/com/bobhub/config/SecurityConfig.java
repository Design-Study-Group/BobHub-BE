package com.bobhub.config;

import com.bobhub.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http, CustomOAuth2UserService customOAuth2UserService) throws Exception {
    http.authorizeHttpRequests(
            auth ->
                auth.requestMatchers(
                        "/", "/login", "/document/**", "/css/**", "/images/**", "/js/**")
                    .permitAll()
                    .anyRequest()
        .oauth2Login(
            oauth2 ->
                oauth2
                    .loginPage("/login")
                    .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                    .defaultSuccessUrl("/", true))
        .logout(
            logout ->
                logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll());
    return http.build();
  }
}
