package com.example.permission.controller;

import com.example.permission.form.AclParam;
import com.example.permission.form.PageQuery;
import com.example.permission.service.SysAclService;
import com.example.permission.util.ResultVOUtil;
import com.example.permission.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author CookiesEason
 * 2019/01/18 14:29
 */
@RequestMapping("/api/sys/acl")
@RestController
public class SysAclController {

    @Autowired
    private SysAclService sysAclService;

    @GetMapping("/save")
    public ResultVO save(AclParam aclParam) {
        sysAclService.save(aclParam);
        return ResultVOUtil.success();
    }

    @GetMapping("/update")
    public ResultVO update(AclParam aclParam) {
        sysAclService.update(aclParam);
        return ResultVOUtil.success();
    }

    @GetMapping("/list")
    public ResultVO list(Integer aclModuleId, PageQuery pageQuery) {
        return ResultVOUtil.success(sysAclService.getPageByAclModuleId(aclModuleId, pageQuery));
    }

}
