package com.govy.common.utils;

import com.govy.common.constant.ResultCodeConstant;
import com.govy.common.constant.ResultConstant;
import com.govy.common.handler.BaseException;
import com.govy.common.result.ResultCodeEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;


public class JwtUtil {

    private static final String signKey = "GovyGovyGovyGovyGovyGovyGovyGovyGovyGovyGovy";
    private static final SecretKey secretKey = Keys.hmacShaKeyFor(signKey.getBytes());
    private static final long EXPIRE = 1000 * 60 * 60 * 24L;

    public static String createJwt(Map<String, Object> claims) {
        return Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .setClaims(claims)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // 解析jwt令牌
    public static Map<String, Object> parseJwt(String jwt) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
            return claims;
        } catch (ExpiredJwtException e) {
            throw new BaseException(ResultCodeEnum.TOKEN_EXPIRED);
        } catch (Exception e) {
            throw new BaseException(ResultCodeEnum.TOKEN_INVALID);
        }
    }
}
