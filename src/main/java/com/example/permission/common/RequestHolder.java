package com.example.permission.common;

import com.example.permission.model.SysUser;

import javax.servlet.http.HttpServletRequest;

/**
 * @author CookiesEason
 * 2019/01/17 14:09
 */
public class RequestHolder {

    private static final ThreadLocal<SysUser> SYS_USER_THREAD_LOCAL = new ThreadLocal<>();

    private static final ThreadLocal<HttpServletRequest> REQUEST_THREAD_LOCAL = new ThreadLocal<>();

    public static void add(SysUser sysUser) {
        SYS_USER_THREAD_LOCAL.set(sysUser);
    }

    public static void add(HttpServletRequest request) {
        REQUEST_THREAD_LOCAL.set(request);
    }

    public static SysUser getUser() {
        return SYS_USER_THREAD_LOCAL.get();
    }

    public static HttpServletRequest getRequest() {
        return REQUEST_THREAD_LOCAL.get();
    }


    public static void remove() {
        REQUEST_THREAD_LOCAL.remove();
        SYS_USER_THREAD_LOCAL.remove();
    }


}
