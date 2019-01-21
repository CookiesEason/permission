package com.example.permission.filter;

import com.example.permission.common.RequestHolder;
import com.example.permission.model.SysUser;
import com.example.permission.util.JsonUtil;
import com.example.permission.util.ResultVOUtil;
import com.example.permission.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author CookiesEason
 * 2019/01/17 14:17
 */
@Slf4j
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
            noAuth(response);
            return;
        }
        RequestHolder.add(sysUser);
        RequestHolder.add(request);
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

    private void noAuth(HttpServletResponse response) {
        ResultVO resultVO = ResultVOUtil.error("未登录无法进行操作");
        response.setContentType("application/json; charset=utf-8");
        try {
            response.getWriter().println(JsonUtil.obj2String(resultVO));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
