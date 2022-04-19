package com.example.auth.services;

import com.example.auth.config.AuthProperties;
import com.example.auth.models.LocalUser;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    private final AuthProperties authProperties;

    public TokenService(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    public String createToken(Authentication authentication) {
        LocalUser userPrincipal = (LocalUser) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + authProperties.getAuth().getTokenExpirationMsec());

        return Jwts.builder().setSubject(Long.toString(userPrincipal.getUser().getId())).setIssuedAt(new Date()).setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, authProperties.getAuth().getTokenSecret()).compact();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(authProperties.getAuth().getTokenSecret()).parseClaimsJws(token).getBody();

        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(authProperties.getAuth().getTokenSecret()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }
}