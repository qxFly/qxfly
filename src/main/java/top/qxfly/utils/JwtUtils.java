package top.qxfly.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class JwtUtils {
    private static final String SignKey = System.getProperty("JwtSignKey");
    private static final String defaultTimeout = System.getProperty("JwtTimeout");

    public static String createToken(int userId, String username, Date date, Long timeout) {
        if (timeout == null){
            timeout = Long.parseLong(defaultTimeout);
        }
        String jwt = Jwts.builder()
                .claim("userId", userId)
                .claim("username", username)
                .setIssuedAt(date) //设置jwt生成时间
                .signWith(SignatureAlgorithm.HS256, SignKey)
                .setExpiration(new Date(System.currentTimeMillis() + timeout))
                .compact();
        return jwt;
    }

    public static Claims parseJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SignKey)
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }
}
