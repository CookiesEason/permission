package com.example.permission.controller;

import com.example.permission.form.PageQuery;
import com.example.permission.form.SearchLogParam;
import com.example.permission.service.SysLogService;
import com.example.permission.util.ResultVOUtil;
import com.example.permission.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author CookiesEason
 * 2019/01/22 10:56
 */
@RequestMapping("/api/sys/log")
@RestController
public class SysLogController {

    @Autowired
    private SysLogService sysLogService;

    @GetMapping("/list")
    public ResultVO list(SearchLogParam searchLogParam, PageQuery pageQuery) {
        return ResultVOUtil.success(sysLogService.getSysLogPageResult(searchLogParam, pageQuery));
    }

    @GetMapping("/recover")
    public ResultVO recover(Integer id) {
        sysLogService.recover(id);
        return ResultVOUtil.success();
    }

}
