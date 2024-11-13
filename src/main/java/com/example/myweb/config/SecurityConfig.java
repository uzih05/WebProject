package com.example.myweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/signup", "/css/**", "/forgot-password", "/reset-password/**").permitAll() // 비밀번호 찾기 페이지와 URL 허용
                        .requestMatchers("/list", "/settings").authenticated() // 인증된 사용자만 접근 가능
                        .anyRequest().authenticated() // 다른 요청도 인증 필요
                )
                .formLogin(form -> form
                        .loginPage("/login") // 로그인 페이지 설정
                        .defaultSuccessUrl("/list") // 로그인 성공 시 이동 경로 설정
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/") // 로그아웃 후 리다이렉트
                        .permitAll()
                );

        return http.build(); // .and()를 사용하지 않고 바로 반환
    }
}