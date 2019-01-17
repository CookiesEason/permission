package com.example.permission.controller;

import com.example.permission.common.RequestHolder;
import com.example.permission.form.PageQuery;
import com.example.permission.form.UserParam;
import com.example.permission.service.SysUserService;
import com.example.permission.util.ResultVOUtil;
import com.example.permission.vo.PageResult;
import com.example.permission.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author CookiesEason
 * 2019/01/15 16:51
 */
@RestController
@RequestMapping("/api/sys/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @RequestMapping("/save")
    public ResultVO save(UserParam userParam) {
        sysUserService.save(userParam);
        return ResultVOUtil.success();
    }

    @RequestMapping("/update")
    public ResultVO update(UserParam userParam) {
        sysUserService.update(userParam);
        return ResultVOUtil.success();
    }

    @GetMapping("/list")
    public ResultVO list(Integer deptId, PageQuery pageQuery) {
        PageResult pageResult = sysUserService.getPageByDeptId(deptId, pageQuery);
        return ResultVOUtil.success(pageResult);
    }

    @GetMapping("/info")
    public ResultVO info() {
        return ResultVOUtil.success(RequestHolder.getUser());
    }

}
