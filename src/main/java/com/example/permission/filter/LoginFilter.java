package com.example.permission.filter;

import com.example.permission.common.RequestHolder;
import com.example.permission.model.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author CookiesEason
 * 2019/01/17 14:17
 */
@Slf4j
@WebFilter(urlPatterns = "/api/sys/*", filterName = "loginFilter")
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("启动登录验证拦截器");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        SysUser sysUser = (SysUser) request.getSession().getAttribute("user");
        if (sysUser == null) {
           log.info("未登录，无法进行操作");
            try {
                returnJson(response, "未登录");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        RequestHolder.add(sysUser);
        RequestHolder.add(request);
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

    private void returnJson(HttpServletResponse response, String json) throws Exception{
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.print(json);

        } catch (IOException e) {
            log.error("response error", e);
        }
    }
}
