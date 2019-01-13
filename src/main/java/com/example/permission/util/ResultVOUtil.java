package com.example.permission.util;

import com.example.permission.vo.ResultVO;

/**
 * @author CookiesEason
 * 2019/01/13 17:59
 */
public class ResultVOUtil {

    public static ResultVO success(Object data, String msg) {
        ResultVO resultVO = new ResultVO();
        resultVO.setRet(true);
        resultVO.setData(data);
        resultVO.setMsg(msg);
        return resultVO;
    }

    public static ResultVO success(Object data) {
        ResultVO resultVO = new ResultVO();
        resultVO.setRet(true);
        resultVO.setData(data);
        return resultVO;
    }

    public static ResultVO success() {
        ResultVO resultVO = new ResultVO();
        resultVO.setRet(true);
        return resultVO;
    }


    public static ResultVO error(String msg) {
        ResultVO resultVO = new ResultVO();
        resultVO.setRet(false);
        resultVO.setMsg(msg);
        return resultVO;
    }



}

