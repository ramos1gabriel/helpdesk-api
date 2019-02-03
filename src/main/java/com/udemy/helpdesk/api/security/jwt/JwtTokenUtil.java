package com.udemy.helpdesk.api.security.jwt;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	static final String CLAIM_KEY_USERNAME = "sub"; //NOME DO USUÁRIO DA CHAVE
	static final String CLAIM_KEY_CREATED = "created"; //CHAVE DE RECLAMAÇÃO CRIADA
	static final String CLAIM_KEY_EXPIRED = "exp"; //CHAVE DE RECLAMAÇÃO EXPIRADA
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private Long expiration;
	
	//Obtem o email de dentro do token (no nosso caso o usuario e email)
	public String getUsernameFromToken(String token) {
		String username;
		
		try {
			final Claims claims = getClaimsFromToken(token);
			username = claims.getSubject();
		} catch (Exception e) {
			username = null;
		}
		
		return username;
	}
	
	//Obtem a data de expiracao de dentro do token
	public java.util.Date getExpirationDateFromToken(String token) {
		java.util.Date expiration;
		
		try {
			final Claims claims = getClaimsFromToken(token);
			expiration = claims.getExpiration();
		} catch(Exception e) {
			expiration = null;
		}
		
		return expiration;
	}
	
	//Metodo que transfere o conteudo do corpo do token
	private Claims getClaimsFromToken(String token) {
		Claims claims;
		
		try {
			claims = Jwts.parser()
					.setSigningKey(secret)
					.parseClaimsJws(token)
					.getBody();
		} catch(Exception e) {
			claims = null;
		}
		
		return claims;
	}
	
	//Verifica se o token esta expirado
	private Boolean isTokenExpired(String token) {
		final java.util.Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new java.util.Date());
	}
	
	//Gera o token
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		
		claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
		
		final java.util.Date createdDate = new java.util.Date();
		claims.put(CLAIM_KEY_CREATED, createdDate);
		
		return doGenerateToken(claims);
	}
	
	//Auxiliar a geracao do token
	private String doGenerateToken(Map<String, Object> claims) {
		final java.util.Date createdDate = (java.util.Date) claims.get(CLAIM_KEY_CREATED);
		final java.util.Date expirationDate = new java.util.Date(createdDate.getTime() + expiration * 1000);
		return Jwts.builder()
				.setClaims(claims)
				.setExpiration(expirationDate)
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();
	}
	
	public Boolean canTokenBeRefreshed(String token) {
		return (!isTokenExpired(token));
	}
	
	//Gera novo tolken a partir da data definida no application
	public String refreshToken(String token) {
		String refreshedToken;
		
		try {
			final Claims claims = getClaimsFromToken(token);
			claims.put(CLAIM_KEY_CREATED, new Date());
			refreshedToken = doGenerateToken(claims);
		} catch(Exception e) {
			refreshedToken = null;
		}
		
		return refreshedToken;
	}
	
	//Realiza a validacao do token a partir do username e data de expiracao
	public Boolean validateToken(String token, UserDetails userDetails) {
		JwtUser user = (JwtUser) userDetails;
		final String username = getUsernameFromToken(token);
		return (username.equals(user.getUsername()) && !isTokenExpired(token));
	}
}
