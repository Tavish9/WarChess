package cn.edu.sustech.cs309.utils;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Slf4j
public class JwtTokenUtil {
    private static final String SECRET = "WarChess";
    private static final long EXPIRATION = 24 * 60 * 60 * 1000L;

    public static String generateToken(String username) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setId(UUID.randomUUID().toString().replace("-", ""))
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS512, generateKey(SECRET))
                .compressWith(CompressionCodecs.GZIP)
                .compact();
    }

    public static Authentication getAuthentication(String token, Collection<? extends GrantedAuthority> authorities){
        if (!validateToken(token))
            throw new RuntimeException("Authenticate fail");
        String username = parseToken(token).getSubject();
        return new UsernamePasswordAuthenticationToken(username, token, authorities);
    }

    public static Claims parseToken(String token){
        return Jwts.parser().setSigningKey(generateKey(SECRET)).parseClaimsJws(token).getBody();
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(generateKey(SECRET)).parseClaimsJws(token).getBody();
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("Request to parse expired JWT : {} failed : {}", token, e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("Request to parse unsupported JWT : {} failed : {}", token, e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("Request to parse invalid JWT : {} failed : {}", token, e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("Request to parse empty or null JWT : {} failed : {}", token, e.getMessage());
        }
        return false;
    }

    public static SecretKey generateKey(String SECRET){
        byte[] encodedKey = Base64.getDecoder().decode(SECRET);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }
}
