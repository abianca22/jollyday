package com.application.security.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private static final String SECRET = "BcyOSQX3jpVcfQJr88yvo86OEBi1S54wHXadnNh4ZdQ0XmI0XQQwv4PLh3OjJHgAkjbnNXaL9amnErMY3IAR0Q";

    public String getUsername(String jwtToken) {
        return getClaim(jwtToken, Claims::getSubject);
    }

    public <T> T getClaim(String jwtToken, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaims(String jwtToken) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }

    public String generateJwtToken(UserDetails user) {
        return generateJwtToken(new HashMap<>(), user);
    }

    public String generateJwtToken(Map<String, Object> extraClaims, UserDetails user) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000)))
                .signWith(getSignInKey())
                .compact();
    }

    private SecretKey getSignInKey() {
        byte[] bytesArr = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(bytesArr);
    }

    public boolean isValid(String jwtToken, UserDetails user) {
        final String username = getUsername(jwtToken);
        return (username.equals(user.getUsername())) && !isExpired(jwtToken);
    }

    private boolean isExpired(String jwtToken) {
        return getExpiration(jwtToken).before(new Date());
    }

    private Date getExpiration(String jwtToken) {
        return getClaim(jwtToken, Claims::getExpiration);
    }

}
