package com.bsdclinic.jwt;

import com.bsdclinic.SecurityConfiguration;
import com.bsdclinic.user.User_;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
@Slf4j
public class JwtService {
    private static final String SECRET_KEY = "TakemyhandtakemywholelifetooforIcanthelpfallinginlovewithyou";
    public static final long JWT_TOKEN_VALIDITY = 1000L;

    public String generateToken(String userId, String username, Integer tokenVersion) {
        return Jwts.builder()
                .claim(User_.TOKEN_VERSION, tokenVersion)
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + JWT_TOKEN_VALIDITY * SecurityConfiguration.TOKEN_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .claim(User_.USER_ID, userId)
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).build().parseSignedClaims(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature -> Message: {0} ", e);
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token -> Message: {0}", e);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token -> Message: {0}", e);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token -> Message: {0}", e);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty -> Message: {0}", e);
        }

        return false;
    }

    public JWTUser getPrincipalFromJwtToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        Integer tokenVersion = claims.get(User_.TOKEN_VERSION, Integer.class);

        return new JWTUser((String) claims.get(User_.USER_ID), claims.getSubject(), tokenVersion);
    }
}

