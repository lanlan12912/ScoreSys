package com.yelanlan.scoremanagersystem.Config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class WebFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(WebFilter.class);
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request= (HttpServletRequest) req;
        HttpServletResponse response= (HttpServletResponse) res;
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        String token = request.getHeader("token");
        //验证是否有token
        if (null == token || token.isEmpty()) {
            throw new ServletException("用户授权认证没有通过!客户端请求参数中无token信息");
        }else {

        }
        logger.info(System.currentTimeMillis()+"跨域请求--------------------------------"+request.getRequestURI());
        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}
