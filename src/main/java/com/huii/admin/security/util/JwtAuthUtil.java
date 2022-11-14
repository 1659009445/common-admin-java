package com.huii.admin.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * jwt util
 * @author hy
 * @version 2.1
 **/
@Component
public class JwtAuthUtil {

    /**
     * 密钥
     */
    @Value("${jwt.key:huiiExampleJwtKey}")
    private String KEY;

    /**
     * 签发者
     */
    @Value("${jwt.issuer:huii}")
    private String ISSUER;

    /**
     * 测试token(min) test
     * 默认1min
     */
    @Value("#{T(java.lang.Integer).parseInt('${jwt.test:1}')}")
    private Integer TEST_TIME;

    /**
     * 单token(h) single
     * 默认24h
     */
    @Value("#{T(java.lang.Integer).parseInt('${jwt.single:24}')}")
    private Integer SINGLE_TIME;

    /**
     * 双token(h) access
     * 默认2h
     */
    @Value("#{T(java.lang.Integer).parseInt('${jwt.access:2}')}")
    private Integer ACCESS_TIME;

    /**
     * 双token(h) refresh
     * 默认168h 7d
     */
    @Value("#{T(java.lang.Integer).parseInt('${jwt.refresh:168}')}")
    private Integer REFRESH_TIME;

    public Integer getTEST_TIME() {
        return TEST_TIME;
    }

    public Integer getSINGLE_TIME() {
        return SINGLE_TIME;
    }

    public Integer getACCESS_TIME() {
        return ACCESS_TIME;
    }

    public Integer getREFRESH_TIME() {
        return REFRESH_TIME;
    }

    /**
     * UserID --> 生成Token
     * @param id Long userID
     * @param time Long Token_time (ms)
     * @return token
     */
    public String createToken(Long id, Long time){
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId",id);

        return  Jwts.builder()
                .setHeaderParam("typ", "JWT")//头部信息
                .signWith(SignatureAlgorithm.HS256, KEY)//加密算法
                .setIssuer(ISSUER)//设置签发者
                .setId(UUID.randomUUID().toString().replaceAll("-", ""))//设置唯一ID
                .setSubject(id.toString())//设置主题
                .setClaims(claims)//设置内容
                .setIssuedAt(new Date())//签发时间
                .setExpiration(new Date(System.currentTimeMillis() + time))//过期时间
                .compact();
    }

    /**
     * 生成TestToken
     * @param id Long userID
     * @return token
     */
    public String createTestToken(Long id){
        return createToken(id,TEST_TIME.longValue() * 60 * 1000);
    }

    /**
     * 生成NormalToken
     * @param id Long userID
     * @return token
     */
    public String createNormalToken(Long id){
        return createToken(id,SINGLE_TIME.longValue() * 60 * 60 * 1000);
    }

    /**
     * 生成AccessToken
     * @param id Long userID
     * @return token
     */
    public String createAccessToken(Long id){
        return createToken(id,ACCESS_TIME.longValue() * 60 * 60 * 1000);
    }

    /**
     * 生成RefreshToken
     * @param id Long userID
     * @return token
     */
    public String createRefreshToken(Long id){
        return createToken(id,REFRESH_TIME.longValue() * 60 * 60 * 1000);
    }

    /**
     * 解析token
     * @param token token
     * @return id(Long)
     */
    public Long parseIdByTokenToLong(String token){
        try {
            Claims claims = Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
            Integer id = (Integer) claims.get("userId");
            return id.longValue();
        }catch (JwtException e) {
            System.out.println("jwt authentication failed!");
            return null;
        }
    }

    /**
     * 解析token
     * @param token token
     * @return id(String)
     */
    public String parseIdByTokenToString(String token){
        try {
            Claims claims = Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
            Integer id = (Integer) claims.get("userId");
            return id.toString();
        }catch (JwtException e) {
            System.out.println("jwt authentication failed!");
            return null;
        }
    }

    /**
     * 判断token是否过期
     * 到期-->true  可用-->false
     * @param token token
     * @return boolean true-->过期
     */
    public Boolean isTokenExpired(String token){
        try {
            Claims claims = Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
            Date expiration = claims.getExpiration();
            System.out.println(expiration);
            System.out.println(new Date());
            return expiration.before(new Date());
        }catch (JwtException e){
            System.out.println("jwt authentication failed!");
            return true;
        }
    }

    /**
     * 判断剩余minute分钟是token是否将要过期
     * @param token token
     * @param minute remain minutes
     * @return boolean true-->将要过期
     */
    public Boolean isTokenWillExpired(String token, Integer minute){
        Claims claims = Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
        Integer now = Integer.valueOf(String.valueOf(new Date().getTime() / 1000));
        Integer exp = (Integer) claims.get("exp");
        int dif = exp - now;
        if (dif < 0)
            return true;
        return dif/60 < minute;
    }

    /**
     * 获取新accessToken
     * @param refresh refreshToken
     * @param access accessToken
     * @param minute remain minutes
     * @return new token
     */
    public String refreshAccessToken(String refresh, String access, Integer minute){
        Long id = parseIdByTokenToLong(refresh);
        if (this.isTokenWillExpired(access,minute))
            return this.createAccessToken(id);
        return null;
    }
}
