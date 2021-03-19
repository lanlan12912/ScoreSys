package com.yelanlan.scoremanagersystem.Config;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WebFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request= (HttpServletRequest) req;
        HttpServletResponse response= (HttpServletResponse) res;
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}
