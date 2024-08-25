package com.example.my.filter;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;

@Component
public class JwtProvider {

	@Value("${spring.security.jwt.key}")
	private String jwtSecrectKey;

    @Value("${spring.security.jwt.expiration}")
    private long expirationTime;

	  private SecretKey key;
	  private KeyPair keyPair;
	  private PrivateKey privateKey;
	  private  PublicKey publicKey;
	   // Initialize the key once after the bean is created
	    @PostConstruct
	    public void init() {
	        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecrectKey));
	         keyPair = Keys.keyPairFor(SignatureAlgorithm.ES512);
	         privateKey = keyPair.getPrivate();
	         publicKey = keyPair.getPublic();

	         
	    }


	public String generateToken(UserDetails userDetails) {

		return Jwts.builder()
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date())
				.setIssuer("Durga")
				.setExpiration(new Date(new Date().getTime() + expirationTime))
	            .signWith(privateKey, SignatureAlgorithm.ES512)  // Using ES512 (asymmetric) algorithm

				.compact();

	}

	public boolean validateToken(String token) {
		try {
			// Parse the token and extract the claims
			Jwts.parser().setSigningKey(key).parseClaimsJws(token);
			return true;

		} catch (SignatureException e) {
			// Invalid JWT signature
			System.out.println("Invalid JWT signature: " + e.getMessage());
		} catch (ExpiredJwtException e) {
			// JWT token is expired
			System.out.println("JWT token is expired: " + e.getMessage());
		} catch (MalformedJwtException e) {
			// Invalid JWT token format
			System.out.println("Invalid JWT token format: " + e.getMessage());
		} catch (UnsupportedJwtException e) {
			// JWT token is unsupported
			System.out.println("JWT token is unsupported: " + e.getMessage());
		} catch (IllegalArgumentException e) {
			// JWT token is empty or null
			System.out.println("JWT token is empty or null: " + e.getMessage());
		} catch (Exception e) {
			// Catch any other exceptions
			System.out.println("JWT token validation failed: " + e.getMessage());
		}
		return false;
	}

	public Claims getTokenDetails(String token) {
		return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody();

	}

	public String getUsernameFromToken(String token) {
		return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody().getSubject();

	}
}
