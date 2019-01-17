package com.example.permission.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

/**
 * @author CookiesEason
 * 2019/01/17 9:00
 */
public class PageQuery {

    @Getter
    @Setter
    @Min(value = 1, message = "当前页面不合法")
    private int pageNo = 1;

    @Getter
    @Setter
    @Min(value = 1, message = "每页展示的数量不合法")
    private int pageSize = 10;

    @Setter
    private int offset;

    public int getOffset() {
        return (pageNo -1) * pageSize;
    }

}
