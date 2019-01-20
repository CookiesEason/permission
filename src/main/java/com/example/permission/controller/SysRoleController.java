package com.example.permission.controller;

import com.example.permission.form.RoleParam;
import com.example.permission.model.SysUser;
import com.example.permission.service.*;
import com.example.permission.util.ResultVOUtil;
import com.example.permission.util.StringUtil;
import com.example.permission.vo.ResultVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Autowired
    private SysRoleUserService sysRoleUserService;

    @Autowired
    private SysUserService sysUserService;

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

    @GetMapping("/changeUsers")
    public ResultVO changeUsers(Integer roleId, String userIds) {
        List<Integer> userIdList = StringUtil.splitToListInt(userIds);
        sysRoleUserService.changeUsers(roleId, userIdList);
        return ResultVOUtil.success();
    }

    @GetMapping("/user")
    public ResultVO users(Integer roleId) {
        List<SysUser> selectedUsers = sysRoleUserService.getListByRoleId(roleId);
        List<SysUser> users = sysUserService.getAll();
        List<SysUser> unSelectedUsers = Lists.newArrayList();
        Set<Integer> selectedUserIdSet = selectedUsers.stream().map(SysUser::getId).collect(Collectors.toSet());
        for (SysUser sysUser : users) {
            if (sysUser.getStatus() == 1 && !selectedUserIdSet.contains(sysUser.getId())) {
                unSelectedUsers.add(sysUser);
            }
        }
        Map<String, List<SysUser>> map = Maps.newHashMap();
        map.put("selectedUsers", selectedUsers);
        map.put("unSelectedUsers", unSelectedUsers);
        return ResultVOUtil.success(map);
    }

}
