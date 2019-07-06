package com.qf;

import com.qf.v13.common.utils.TokenUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jdk.nashorn.internal.parser.Token;
import org.junit.Test;

import java.util.Date;

/**
 * @author maizifeng
 * @Date 2019/6/28
 */
public class JJWTTest {

    @Test
    public void test1(){
        String token = TokenUtils.createJwtToken("1", null, "admin", 30 * 1000);
        System.out.println(token);
    }

    @Test
    public void parse(){
        String token = TokenUtils.createJwtToken("1", null, "admin", 30 * 1000);
        Claims claims = null;
        String newToken = null;
        try {
            claims = TokenUtils.parseJWT(token);
        } catch (Exception e) {
            newToken = TokenUtils.createJwtToken(null, null, null, 10 * 1000);
        }
        System.out.println(claims);
        System.out.println(newToken);
    }

    @Test
    public void isExpired() throws InterruptedException {
        String token = TokenUtils.createJwtToken("1", null, "admin", 30 * 1000);
        System.out.println(token);
        Claims claims = Jwts.parser().setSigningKey("admin").parseClaimsJws(token).getBody();
        Date expiration = claims.getExpiration();
        long expirationTime = expiration.getTime();

        System.out.println(expirationTime);
        System.out.println(System.currentTimeMillis());
        long currentTimeMillis = System.currentTimeMillis();
        Thread.sleep(10000);
        if(expirationTime>currentTimeMillis){
             token = TokenUtils.createJwtToken("1", null, "admin", 30 * 1000);
            System.out.println(token);
        }
         claims = Jwts.parser().setSigningKey("admin").parseClaimsJws(token).getBody();

        expiration = claims.getExpiration();
        expirationTime = expiration.getTime();
        System.out.println(expirationTime);
    }

}
