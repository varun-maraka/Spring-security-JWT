package io.javabrains.security;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.javabrains.service.MyUserDetailsService;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private MyUserDetailsService myUserDetailsService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(myUserDetailsService);
	}
	@Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
	
	@Override
	protected void configure(HttpSecurity httpSec) throws Exception{
		httpSec.authorizeRequests()
				.antMatchers("/").permitAll()
				.antMatchers("/authenticate").permitAll()
				.antMatchers("/auth").authenticated()
				.antMatchers("/user").hasAnyRole("USER","ADMIN")
				.antMatchers("/admin").hasRole("ADMIN")
				.antMatchers("/h2-console/**").permitAll().anyRequest().authenticated() 
				.and().formLogin();
		
		httpSec.headers().frameOptions().sameOrigin();
		httpSec.csrf().disable(); 
		// This code make h2 database console available to access
		//httpSec.headers().frameOptions().disable();
	}
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception{
		return super.authenticationManager();
	}
}
