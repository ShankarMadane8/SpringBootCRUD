package com.crud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class MyConfig {
	
	@Bean
	public UserDetailsService getUserDetailsService() {
		return new UserDetailServiceImp();		
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider=new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(getUserDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	
	
//	@Bean
//	public AuthenticationManager authenticationManager(
//	        AuthenticationConfiguration authConfig) throws Exception {
//	    return authConfig.getAuthenticationManager();
//	}
	
	
	@Bean 
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authenticationProvider(authenticationProvider());
		
		http.authorizeRequests()
		.antMatchers("/admin/**").hasAuthority("admin")
		.antMatchers("/normal/**").hasAnyAuthority("normal","admin")
		.antMatchers("/special/**").hasAnyAuthority("special","admin")
		.antMatchers("/**").permitAll().and()
		.formLogin().loginPage("/login").usernameParameter("email")
		.defaultSuccessUrl("/normal/dashboard", true).and()
		.csrf().disable();

		
		/*
		 * http.authorizeRequests().antMatchers("/login").permitAll()
		 * .antMatchers("/users/**", "/settings/**").hasAuthority("Admin")
		 * .hasAnyAuthority("Admin", "Editor", "Salesperson") .hasAnyAuthority("Admin",
		 * "Editor", "Salesperson", "Shipper") .anyRequest().authenticated()
		 * .and().formLogin() .loginPage("/login") .usernameParameter("email")
		 * .permitAll() .and() .rememberMe().key("AbcdEfghIjklmNopQrsTuvXyz_0123456789")
		 * .and() .logout().permitAll();
		 * 
		 * http.headers().frameOptions().sameOrigin();
		 */
	
		return http.build();
	}

}
