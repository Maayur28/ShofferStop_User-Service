package com.userservice.utils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import com.userservice.entity.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtTokenUtil implements Serializable {

	private static final long serialVersionUID = -8850702972103995712L;

	private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

	public Map<String, String> generateToken(UserEntity userEntity, boolean remember) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, userEntity.getUserId(), remember);
	}

	public Map<String, String> doGenerateToken(Map<String, Object> claims, String subject, boolean remember) {
		Map<String, String> tokens = new HashMap<>();
		long accessTokenExpirationTime = 12, refreshTokenExpirationTime = 24;
		if (remember) {
			refreshTokenExpirationTime = 24 * 7;
		}
		tokens.put("accessToken",
				Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
						.setExpiration(new Date(System.currentTimeMillis() + accessTokenExpirationTime * 3600 * 1000))
						.signWith(SECRET_KEY).compact());
		tokens.put("refreshToken",
				Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
						.setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpirationTime * 3600 * 1000))
						.signWith(SECRET_KEY).compact());
		return tokens;
	}

	public Boolean validateToken(String token) {
		try {
			Claims claims = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
			Boolean isTokenExpired = claims.getExpiration().before(new Date());
			return isTokenExpired;
		} catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
			throw new BadCredentialsException("INVALID_CREDENTIALS", ex);
		} catch (ExpiredJwtException ex) {
			throw ex;
		}
	}

	public String getUserIdFromToken(String token) {
		final Claims claims = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
		return claims.getSubject();
	}
}