package com.example.permission.controller;

import com.example.permission.exception.ParamException;
import com.example.permission.form.TestForm;
import com.example.permission.util.ResultVOUtil;
import com.example.permission.util.ValidatorUtil;
import com.example.permission.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;


/**
 * @author CookiesEason
 * 2019/01/13 18:25
 */
@Controller
@RequestMapping("/test")
@Slf4j
public class TestController {

    @GetMapping("/hello.json")
    @ResponseBody
    public ResultVO hello() {
        throw new RuntimeException("test exception");
//        return ResultVOUtil.success("123123");
    }

    @GetMapping("/validate.json")
    @ResponseBody
    public ResultVO validate(@Valid TestForm testForm, BindingResult bindingResult) throws ParamException {
        ValidatorUtil.checkBySelf(bindingResult);
        return ResultVOUtil.success(testForm.toString());
    }

}
