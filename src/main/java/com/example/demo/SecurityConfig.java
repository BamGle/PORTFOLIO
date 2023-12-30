package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.demo.user.UserRole;

/*
 * 스프링 시큐리티 설정파일
 */
@Configuration
@EnableWebSecurity   // --> 모든 요청 url이 스프링 시큐리티의 제어를 받도록 만드는 애너테이션
@EnableMethodSecurity(prePostEnabled = true)  // @PreAuthorize 동작하도록 셋팅
public class SecurityConfig {	
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception{	
		
		http.authorizeHttpRequests(
			(authorizeHttpRequests) ->authorizeHttpRequests
//			.requestMatchers(new AntPathRequestMatcher("/**")).permitAll()  // 인증되지 않은 모든경로는 허용한다.
			
			.requestMatchers(new AntPathRequestMatcher("/error")).permitAll()
			.requestMatchers(new AntPathRequestMatcher("/css/**")).permitAll()
			.requestMatchers(new AntPathRequestMatcher("/images/**")).permitAll()
			.requestMatchers(new AntPathRequestMatcher("/js/**")).permitAll()
			.requestMatchers(new AntPathRequestMatcher("/plugins/**")).permitAll()
			
			
			.requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()			
			.requestMatchers(new AntPathRequestMatcher("/")).permitAll()
			.requestMatchers(new AntPathRequestMatcher("/user/**")).permitAll()  // 루트와 로그인 회원가입 로그아웃은 제외			
			.requestMatchers(new AntPathRequestMatcher("/question/**")).hasAnyRole("ADMIN","USER")
			.requestMatchers(new AntPathRequestMatcher("/answer/**")).hasAnyRole("ADMIN","USER")
			)
		.csrf((csrf) -> csrf.ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")))
		.headers((headers) -> headers
                .addHeaderWriter(new XFrameOptionsHeaderWriter(
                    XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
		.formLogin(
				(formlogin) ->formlogin.loginPage("/user/login")
				.defaultSuccessUrl("/")
				)
		.logout(
				(logout)->logout.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
				.logoutSuccessUrl("/")
				.invalidateHttpSession(true)
				)		
		;
		return http.build();
		
	}
	
//	암호화 빈
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
// 인증을 담당할 빈
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
		return authenticationConfiguration.getAuthenticationManager();
	}
}
