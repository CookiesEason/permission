package com.example.permission.controller;

import com.example.permission.form.AclModuleParam;
import com.example.permission.service.SysAclModuleService;
import com.example.permission.service.SysTreeService;
import com.example.permission.util.ResultVOUtil;
import com.example.permission.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author CookiesEason
 * 2019/01/17 18:23
 */
@RestController
@RequestMapping("/api/sys/aclModule")
public class SysAclModuleController {

    @Autowired
    private SysAclModuleService aclModuleService;

    @Autowired
    private SysTreeService sysTreeService;

    @GetMapping("/save")
    public ResultVO save(AclModuleParam aclModuleParam) {
        aclModuleService.save(aclModuleParam);
        return ResultVOUtil.success();
    }

    @GetMapping("/update")
    public ResultVO update(AclModuleParam aclModuleParam) {
        aclModuleService.update(aclModuleParam);
        return ResultVOUtil.success();
    }

    @GetMapping("/tree")
    public ResultVO tree() {
        return ResultVOUtil.success(sysTreeService.aclModuleTree());
    }

    @GetMapping("/delete")
    public ResultVO deleteAclModule(Integer id) {
        aclModuleService.delete(id);
        return ResultVOUtil.success();
    }

}
