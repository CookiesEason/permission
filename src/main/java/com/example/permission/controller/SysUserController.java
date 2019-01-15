package com.example.permission.controller;

import com.example.permission.form.UserParam;
import com.example.permission.service.SysUserService;
import com.example.permission.util.ResultVOUtil;
import com.example.permission.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author CookiesEason
 * 2019/01/15 16:51
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @RequestMapping("/save.json")
    public ResultVO save(UserParam userParam) {
        sysUserService.save(userParam);
        return ResultVOUtil.success();
    }

    @RequestMapping("/update.json")
    public ResultVO update(UserParam userParam) {
        sysUserService.update(userParam);
        return ResultVOUtil.success();
    }

}
