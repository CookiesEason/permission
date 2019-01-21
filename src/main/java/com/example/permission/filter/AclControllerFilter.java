package com.example.permission.filter;

import com.example.permission.common.ApplicationContextHelper;
import com.example.permission.common.RequestHolder;
import com.example.permission.model.SysUser;
import com.example.permission.service.SysCoreService;
import com.example.permission.util.JsonUtil;
import com.example.permission.util.ResultVOUtil;
import com.example.permission.vo.ResultVO;
import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author CookiesEasona
 * 2019/01/21 10:32
 */
@Slf4j
public class AclControllerFilter implements Filter {

    private static Set<String> exclusionUrlSet = Sets.newConcurrentHashSet();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("AclControllerFilter权限过滤器启动");
        String exclusionUrls = filterConfig.getInitParameter("exclusionUrls");
        log.info("不过滤的urls{}",exclusionUrls);
        List<String> urls = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(exclusionUrls);
        exclusionUrlSet = Sets.newConcurrentHashSet(urls);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String url = request.getServletPath();
        Map map = request.getParameterMap();
        if (exclusionUrlSet.contains(url)) {
            filterChain.doFilter(servletRequest, servletResponse);
        }
        SysUser sysUser = RequestHolder.getUser();
        if (sysUser == null) {
            log.info("尝试访问{}, 但是没登录, 参数{}", request.getServletPath(), JsonUtil.obj2String(map));
            noAuth(response);
            return;
        }
        SysCoreService sysCoreService = ApplicationContextHelper.popBean("sysCoreService", SysCoreService.class);
        if (!sysCoreService.hasUrlAcl(url)) {
            log.info("{}尝试访问{}, 但是没权限, 参数{}",JsonUtil.obj2String(sysUser),
                    request.getServletPath(), JsonUtil.obj2String(map));
            noAuth(response);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }

    private void noAuth(HttpServletResponse response) {
        ResultVO resultVO = ResultVOUtil.error("无权限访问");
        response.setContentType("application/json; charset=utf-8");
        try {
            response.getWriter().println(JsonUtil.obj2String(resultVO));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
