package com.example.UserTest;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.io.File;
import java.util.InputMismatchException;
import java.util.Scanner;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests()
				.antMatchers( "/result.html").permitAll()
				.antMatchers("/signup.html").permitAll()
				.antMatchers("/report.html").permitAll()
				.antMatchers("/index.js").permitAll()
				.antMatchers("/mapsStyle.css").permitAll()
				.antMatchers("/login.html").permitAll()
				.antMatchers("/index.html").permitAll()
				.antMatchers( "/").permitAll()
				.antMatchers( "/public/**").permitAll()
				.antMatchers("/map.html").permitAll()
				.antMatchers("/resources/**").permitAll()
				.anyRequest().authenticated()
				.and()
				.formLogin()
				.loginPage("/login.html")
				.failureUrl("/login-error.html")
				.permitAll();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> config = auth.inMemoryAuthentication();
		Scanner sc;
		try{
			File users = new File("Users.txt");
			sc = new Scanner(users);
			while(sc.hasNextLine()){
				// user
				String user = sc.next();
				// password
				String password = sc.next();
				// setup account
				sc.nextLine();
				config.withUser(user).password("{noop}" + password).roles("USER");
			}
			sc.close();
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

}

