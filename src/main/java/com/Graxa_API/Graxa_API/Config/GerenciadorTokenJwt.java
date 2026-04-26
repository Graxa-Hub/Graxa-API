package com.Graxa_API.Graxa_API.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.function.Function;


public class GerenciadorTokenJwt {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.validity}")
    private Long jwtTokenValidity;

    public String getUsernameFromToken(String token){
        return getClaimForToken(token, Claims:: getSubject);
    }

    public Date getExpirationDateFromToken(String token){
        return getClaimForToken(token, Claims::getExpiration);
    }

    public String generateToken(final Authentication authentication) {
        final String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("roles", authorities)           // ← adiciona essa linha
                .signWith(parseSecret())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtTokenValidity * 1_000))
                .compact();
    }

    // Adiciona esse método novo
    public String getRolesFromToken(String token) {
        return getClaimForToken(token, claims -> claims.get("roles", String.class));
    }

    public <T> T getClaimForToken(String token, Function<Claims, T> claimsResolver){
        Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public boolean validaeToke(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(new Date(System.currentTimeMillis()));
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(parseSecret())
                .build()
                .parseClaimsJws(token).getBody();
    }

    public boolean validaTokenSomente(String token, String username) {
        try {
            String usernameToken = getUsernameFromToken(token);
            return usernameToken.equals(username) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    private SecretKey parseSecret() {
        return Keys.hmacShaKeyFor(this.secret.getBytes(StandardCharsets.UTF_8));
    }
}
