package com.example.permission.controller;

import com.example.permission.form.RoleParam;
import com.example.permission.service.SysRoleAclService;
import com.example.permission.service.SysRoleService;
import com.example.permission.service.SysTreeService;
import com.example.permission.util.ResultVOUtil;
import com.example.permission.util.StringUtil;
import com.example.permission.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author CookiesEason
 * 2019/01/18 21:11
 */
@RequestMapping("/api/sys/role")
@RestController
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysTreeService sysTreeService;

    @Autowired
    private SysRoleAclService sysRoleAclService;

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


    @GetMapping("/roleTree")
    public ResultVO tree(Integer roleId) {
        return ResultVOUtil.success(sysTreeService.roleTree(roleId));
    }

    @GetMapping("/changeAcls")
    public ResultVO tree(Integer roleId, String aclIds) {
        sysRoleAclService.changeRoleAcls(roleId, StringUtil.splitToListInt(aclIds));
        return ResultVOUtil.success();
    }

}
