package com.app.security.service;

import com.app.security.filter.JwtAuthenticationFilter;
import com.app.security.filter.JwtAuthorizationFilter;
import com.app.security.token.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	private final UserDetailsService userDetailsService;
	private final TokenService tokenService;

	public SecurityConfiguration(
			@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService,
			TokenService tokenService) {
		this.userDetailsService = userDetailsService;
		this.tokenService = tokenService;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
				.userDetailsService(userDetailsService)
				.passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.csrf().disable()

				.exceptionHandling()
				.authenticationEntryPoint(authenticationEntryPoint())
				.accessDeniedHandler(accessDeniedHandler())

				.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)

				.and()
				.authorizeRequests()
				.antMatchers("/security/**").permitAll()
				.antMatchers("/products/**").hasAnyRole("USER")
				.antMatchers("/customers/**").hasAnyRole("ADMIN")
				.antMatchers("/orders/**").hasAnyRole("ADMIN")
				.anyRequest().authenticated()

				.and()
				.addFilter(new JwtAuthenticationFilter(authenticationManager(), tokenService))
				.addFilter(new JwtAuthorizationFilter(authenticationManager(), tokenService));
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	private AuthenticationEntryPoint authenticationEntryPoint() {
		return (httpServletRequest, httpServletResponse, e) -> {
			httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
			httpServletResponse.getWriter().write(new ObjectMapper().writeValueAsString(e.getMessage()));
			httpServletResponse.getWriter().flush();
			httpServletResponse.getWriter().close();
		};
	}

	private AccessDeniedHandler accessDeniedHandler() {
		return (httpServletRequest, httpServletResponse, e) -> {
			httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
			httpServletResponse.getWriter().write(new ObjectMapper().writeValueAsString(e.getMessage()));
			httpServletResponse.getWriter().flush();
			httpServletResponse.getWriter().close();
		};
	}
}
