package com.yelanlan.scoremanagersystem.Config;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.yelanlan.scoremanagersystem.RepositoryImpl.User;
import com.yelanlan.scoremanagersystem.ServiceImpl.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    Environment environment;
    @Autowired
    UserService userService;
    private final static Logger logger = LogManager.getLogger();
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        try {
            // 如果不是映射到方法直接通过
            if(!(object instanceof HandlerMethod)){
                return true;
            }
            String uri = request.getRequestURI();
            List<String> uris = Arrays.asList(uri.split("\\,"));
            if(uris.contains(environment.getProperty("server.servlet.context-path")+"/login")){
                return true;//绕过登录页面
            }else {
                Cookie[] cookie = request.getCookies();
                String token = String.valueOf(cookie[0].getValue());// 从 http 请求头中取出 token
                if(null != token){
                    //从token中中获取userNumber
                    String userNumber =  JWT.decode(token).getClaim("userNumber").asString();
                    User user = userService.findUserByNumber(userNumber);
                    if(null != user){
                        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(user.getUserPwd()))
                                .withClaim("userNumber", userNumber)
                                .build();
                        // 效验TOKEN
                        DecodedJWT jwt = verifier.verify(token);
                        return true;
                    }
                }
            }
            response.setStatus(401);//请求要求身份验证。 对于需要登录的网页，服务器可能返回此响应。
            return false;
        }catch (Exception e){
            logger.error("身份验证失败",e.getMessage());
            response.setStatus(403);//未满足前提条件） 服务器未满足请求者在请求中设置的其中一个前提条件。
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) throws Exception {

    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {

    }
}
