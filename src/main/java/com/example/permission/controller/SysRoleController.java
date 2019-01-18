package com.example.permission.controller;

import com.example.permission.form.RoleParam;
import com.example.permission.service.SysRoleService;
import com.example.permission.util.ResultVOUtil;
import com.example.permission.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author CookiesEason
 * 2019/01/18 21:11
 */
@RequestMapping("/api/sys/role")
@RestController
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @GetMapping("/save")
    public ResultVO save(RoleParam roleParam) {
        sysRoleService.save(roleParam);
        return ResultVOUtil.success();
    }

    @GetMapping("/update")
    public ResultVO update(RoleParam roleParam) {
        sysRoleService.update(roleParam);
        return ResultVOUtil.success();
    }

    @GetMapping("/list")
    public ResultVO list() {
        return ResultVOUtil.success(sysRoleService.getAll());
    }

}
