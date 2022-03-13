package com.echo.service;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
//@WebFilter(filterName = "myfilter2",urlPatterns ={"/jsp/*","/jsp/**/*"})
public class MyFilter2 implements Filter {
//    private Logger logger = Logger.getLogger(LoginFilter.class);
    private String[] ignoreArr=null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ignoreArr=filterConfig.getInitParameter("ignore").split(",");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req=(HttpServletRequest) servletRequest;
        HttpServletResponse resp=(HttpServletResponse) servletResponse;
        boolean flag=isIgnore(req);
        if(flag) {
            filterChain.doFilter(req, resp);
        }else {
                HttpSession session = req.getSession();
                if(session.getAttribute("loginUser")==null){
                    resp.sendRedirect("/error.jsp");
                }
        }
    }
    public boolean isIgnore(HttpServletRequest request) {
        String path=request.getRequestURI().toLowerCase();
        for(String ignore:ignoreArr) {
            if(path.contains(ignore)) {
                return true;
            }
        }
        return false;
    }
}
