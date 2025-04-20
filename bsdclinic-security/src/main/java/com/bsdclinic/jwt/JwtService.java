package com.bsdclinic.jwt;

import com.bsdclinic.SecurityConfiguration;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRET_KEY = "TakemyhandtakemywholelifetooforIcanthelpfallinginlovewithyou";
    public static final long JWT_TOKEN_VALIDITY = 1000L;
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class.getName());

    public String generateToken(String userId, String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + JWT_TOKEN_VALIDITY * SecurityConfiguration.tokenExpirationTime))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .claim("userId", userId)
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
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

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).build().parseSignedClaims(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature -> Message: {0} ", e);
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token -> Message: {0}", e);
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token -> Message: {0}", e);
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token -> Message: {0}", e);
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty -> Message: {0}", e);
        }

        return false;
    }

    public JWTUser getPrincipalFromJwtToken(String token) {
        Claims body = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return new JWTUser((String) body.get("userId"), body.getSubject());
    }
}

