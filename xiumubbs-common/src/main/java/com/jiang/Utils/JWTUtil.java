package com.jiang.Utils;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtil {
    private static final String jwtToken = "123456";

    public static String createToken(String userId){
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId",userId);
        JwtBuilder jwtBuilder = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256,jwtToken)//签发算法
                .setClaims(claims)//内容
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+24*60*60*60*1000));//设置过期时间一天有效
        String token = jwtBuilder.compact();
        return token;
    }

    public static Map<String,Object> checkToken(String token){
        try {
            Jwt parse = Jwts.parser().setSigningKey(jwtToken).parse(token);
            return (Map<String,Object>)parse.getBody();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
