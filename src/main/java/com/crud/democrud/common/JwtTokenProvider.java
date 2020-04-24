package com.crud.democrud.common;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.crud.democrud.user.User;
import com.crud.democrud.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Autowired
    private UserService userService;

    final private static long JWT_EXPIRY_TIME = 1 * 60 * 60;

    public String generateJWT(User user) {
        Claims claims = Jwts.claims();
        claims.setSubject(user.getEmail());
        claims.put("role", new ArrayList<>(Arrays.asList("user")));
        claims.setIssuedAt(new Date());
        claims.setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRY_TIME * 1000));

        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    }

    public String getUserEmail(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean isTokenValid(String token) throws Exception {
        try {
            Date expiryDate = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getExpiration();
            if (expiryDate.before(new Date())) {
                return false;
            }
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            e.printStackTrace();
            throw new Exception("Invalid JWT / Expired token");
        }
    }

    public String getTokenFromHeaders(HttpServletRequest request) {
        String authToken = request.getHeader("Authorization");
        if (authToken != null && authToken.startsWith("Bearer")) {
            return authToken.substring(7);
        }
        return null;
    }

    public Authentication getAuthentication(String token) {
        User user = userService.findByEmail(getUserEmail(token));
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("USER"));
        return new UsernamePasswordAuthenticationToken(user, "", grantedAuthorities);
    }

}