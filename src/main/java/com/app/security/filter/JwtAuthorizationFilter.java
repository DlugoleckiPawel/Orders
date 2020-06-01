package com.app.security.filter;

import com.app.security.token.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
	private TokenService tokenService;

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, TokenService tokenService) {
		super(authenticationManager);
		this.tokenService = tokenService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,
									HttpServletResponse response,
									FilterChain chain) throws IOException, ServletException {
		String token = request.getHeader("Authorization");

		if (Objects.nonNull(token)) {
			UsernamePasswordAuthenticationToken auth = tokenService.parseToken(token);
			SecurityContextHolder.getContext().setAuthentication(auth);
		}
		chain.doFilter(request, response);
	}
}
