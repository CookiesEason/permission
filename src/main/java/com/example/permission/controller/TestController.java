package com.example.permission.controller;

import com.example.permission.exception.PermissionException;
import com.example.permission.util.ResultVOUtil;
import com.example.permission.vo.ResultVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author CookiesEason
 * 2019/01/13 18:25
 */
@Controller
@RequestMapping("/test")
public class TestController {

    @GetMapping("/hello.json")
    @ResponseBody
    public ResultVO hello() {
        throw new RuntimeException("test exception");
//        return ResultVOUtil.success("123123");
    }

}
