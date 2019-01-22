package com.example.permission.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author CookiesEason
 * 2019/01/22 11:31
 * 时间格式： yyyy-MM-dd HH:mm:ss
 */
@Getter
@Setter
@ToString
public class SearchLogParam {

    private  Integer type;

    private String beforeSeq;

    private String afterSeq;

    private String operator;

    private String fromTime;

    private String toTime;

}
