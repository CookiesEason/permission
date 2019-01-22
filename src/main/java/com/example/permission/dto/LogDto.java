package com.example.permission.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author CookiesEason
 * 2019/01/22 11:37
 */
@Getter
@Setter
@ToString
public class LogDto {

    private Integer type;

    private String beforeSeg;

    private String afterSeg;

    private String operator;

    private Date fromTime;

    private Date toTime;

}
