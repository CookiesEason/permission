package com.example.permission.controller;

import com.example.permission.form.DeptParam;
import com.example.permission.service.SysDeptService;
import com.example.permission.service.SysTreeService;
import com.example.permission.util.ResultVOUtil;
import com.example.permission.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author CookiesEason
 * 2019/01/14 16:12
 */
@Controller
@RequestMapping("/sys/dept")
public class SysDeptController {

    @Autowired
    private SysDeptService sysDeptService;

    @Autowired
    private SysTreeService sysTreeService;

    @RequestMapping("/save.json")
    @ResponseBody
    public ResultVO saveDept(DeptParam deptParam) {
        sysDeptService.save(deptParam);
        return ResultVOUtil.success();
    }

    @RequestMapping("/tree.json")
    @ResponseBody
    public ResultVO tree() {
        return ResultVOUtil.success(sysTreeService.deptTree());
    }

    @RequestMapping("/update.json")
    @ResponseBody
    public ResultVO updateDept(DeptParam deptParam) {
        sysDeptService.update(deptParam);
        return ResultVOUtil.success();
    }

}
