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
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTYxNzIxMDM0LCJzdWIiOiJhZG1pbiIsImV4cCI6MTU2MTcyMTA2NH0.dMN6jRIUAhcJgr7HTkJct208f4N3s_RAIOUUbYHLtUs";
        Claims claims = null;
        String newToken = null;
        try {
            claims = TokenUtils.parseJWT(token);
        } catch (Exception e) {
            newToken = TokenUtils.createJwtToken(null, null, null, 10 * 1000);
        }
        System.out.println(newToken);
    }

    @Test
    public void isExpired(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwic3ViIjoidGVzdCIsImV4cCI6MTU2MTcxNTQ0NH0.Da58xaQDeN8fnLeCX40IUoMMCJCvBovlxH2Ci6TOdyk";
        Claims claims = Jwts.parser().setSigningKey("java1902").parseClaimsJws(token).getBody();
        Date expiration = claims.getExpiration();
        long expirationTime = expiration.getTime();

        System.out.println(expirationTime);
        System.out.println(System.currentTimeMillis());
        long currentTimeMillis = System.currentTimeMillis();
        if(expirationTime<currentTimeMillis){

        }
        boolean before = expiration.before(new Date());
        System.out.println(before);

    }

}
