package com.example.permission.controller;

import com.example.permission.model.SysUser;
import com.example.permission.service.SysUserService;
import com.example.permission.util.MD5Util;
import com.example.permission.util.ResultVOUtil;
import com.example.permission.vo.ResultVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author CookiesEason
 * 2019/01/15 19:10
 */
@RequestMapping("/user")
@RestController
public class UserController {

    @Resource
    private SysUserService sysUserService;

    @RequestMapping("/login")
    public ResultVO login(HttpServletRequest request) {
        return sysUserService.login(request);
    }

    @RequestMapping("/logout")
    public ResultVO logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return ResultVOUtil.success();
    }

}
