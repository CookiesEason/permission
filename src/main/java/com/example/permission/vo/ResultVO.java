package com.example.permission.vo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author CookiesEason
 * 2019/01/13 17:56
 */
@Data
public class ResultVO {

    private Boolean ret;

    private String msg;

    private Object data;


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>(16);
        result.put("ret", ret);
        result.put("msg", msg);
        result.put("data", data);
        return result;
    }

}
