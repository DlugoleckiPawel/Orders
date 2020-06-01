package com.app.security.token;

import com.app.dto.TokensDto;
import com.app.exceptions.AuthenticationException;
import com.app.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TokenService {

	@Value("${tokens.access.expiration-time-ms}")
	private Long accessTokenExpirationTimeMs;

	@Value("${tokens.refresh.expiration-time-ms}")
	private Long refreshTokenExpirationTimeMs;

	@Value("${tokens.prefix}")
	private String tokenPrefix;

	private final UserRepository userRepository;
	private final SecretKey secretKey;

	public TokensDto generateTokens(Authentication authentication) {

		if (Objects.isNull(authentication)) {
			throw new AuthenticationException("authentication object is null");
		}

		var user = userRepository
				.findByUsername(authentication.getName())
				.orElseThrow(() -> new AuthenticationException("cannot get user from db"));
		var userId = String.valueOf(user.getId());

		var creationDate = new Date();
		var expirationAccessTokenTimeMs = System.currentTimeMillis() + accessTokenExpirationTimeMs;
		var expirationAccessTokenDate = new Date(expirationAccessTokenTimeMs);
		var expirationRefreshTokenDate = new Date(refreshTokenExpirationTimeMs);

		var accessToken = Jwts
				.builder()
				.setSubject(userId)
				.setIssuedAt(creationDate)
				.setExpiration(expirationAccessTokenDate)
				.signWith(secretKey)
				.compact();

		var refreshToken = Jwts
				.builder()
				.setSubject(userId)
				.setIssuedAt(creationDate)
				.setExpiration(expirationRefreshTokenDate)
				.signWith(secretKey)
				.compact();

		return TokensDto
				.builder()
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.build();
	}

	public UsernamePasswordAuthenticationToken parseToken(String token) {
		if (Objects.isNull(token)) {
			throw new AuthenticationException("Token is null");
		}
		if (!token.startsWith(tokenPrefix)) {
			throw new AuthenticationException("token prefix is not correct");
		}

		var accessToken = token.replace(tokenPrefix, "");

		if (!isTokenValid(accessToken)) {
			throw new AuthenticationException("token is not valid");
		}

		Long userId = getId(accessToken);
		var user = userRepository
				.findOneById(userId)
				.orElseThrow(() -> new AuthenticationException("parse token - can not find user"));

		return new UsernamePasswordAuthenticationToken(
				user.getUsername(),
				null,
				List.of(new SimpleGrantedAuthority(user.getRoles().toString())));
	}

	private Claims getClaims(String token) {
		if (Objects.isNull(token)) {
			throw new AuthenticationException("token is null");
		}
		return Jwts
				.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	private Long getId(String token) {
		return Long.parseLong(getClaims(token).getSubject());
	}

	private Date getExpirationDate(String token) {
		return getClaims(token).getExpiration();
	}

	private boolean isTokenValid(String token) {
		Date expirationDate = getExpirationDate(token);
		return expirationDate.after(new Date());
	}
}
