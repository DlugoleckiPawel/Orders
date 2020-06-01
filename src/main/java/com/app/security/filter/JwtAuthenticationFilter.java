package com.app.security.filter;

import com.app.dto.AuthRequestDto;
import com.app.dto.TokensDto;
import com.app.exceptions.AuthenticationException;
import com.app.security.token.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private final TokenService tokenService;

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager, TokenService tokenService) {
		this.authenticationManager = authenticationManager;
		this.tokenService = tokenService;
	}

	@Override
	public Authentication attemptAuthentication(
			HttpServletRequest request,
			HttpServletResponse response) {
		try {
			AuthRequestDto auth = new ObjectMapper()
					.readValue(request.getInputStream(), AuthRequestDto.class);

			return authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(
							auth.getUsername(),
							auth.getPassword(),
							Collections.emptyList()
					));
		} catch (Exception e) {
			throw new AuthenticationException("authentication error");
		}
	}

	@Override
	protected void successfulAuthentication(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		TokensDto tokensDto = tokenService.generateTokens(authResult);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write(new ObjectMapper().writeValueAsString(tokensDto));
		response.getWriter().flush();
		response.getWriter().close();
	}
}
