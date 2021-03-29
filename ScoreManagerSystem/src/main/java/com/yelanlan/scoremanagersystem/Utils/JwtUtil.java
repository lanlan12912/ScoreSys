package com.yelanlan.scoremanagersystem.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.yelanlan.scoremanagersystem.RepositoryImpl.User;

import java.util.Date;

public class JwtUtil {
    /**
     * 签名生成
     * @param user
     * @return
     */
    public static String getToken(User user) {
        Date start = new Date();
        long currentTime = System.currentTimeMillis() + 60* 60 * 1000;//一小时有效时间
        Date end = new Date(currentTime);
        String token="";
        token= JWT.create()
                .withClaim("userNumber",user.getUserNumber())
                .withIssuedAt(start)
                .withExpiresAt(end)
                .sign(Algorithm.HMAC256(user.getUserPwd()));//使用了HMAC256加密算法。
        return token;
    }
}
