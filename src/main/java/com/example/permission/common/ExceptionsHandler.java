package com.example.permission.common;

import com.example.permission.exception.ParamException;
import com.example.permission.exception.PermissionException;
import com.example.permission.util.ResultVOUtil;
import com.example.permission.vo.ResultVO;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author CookiesEason
 * 2019/01/15 18:53
 */
@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(value = PermissionException.class)
    @ResponseBody
    public ResultVO handlePermissionException(PermissionException e){
        return ResultVOUtil.error(e.getMessage());
    }

    @ExceptionHandler(value = ParamException.class)
    @ResponseBody
    public ResultVO handleParamException(ParamException e){
        return ResultVOUtil.error(e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultVO handleException(){
        return ResultVOUtil.error("System error");
    }

}
