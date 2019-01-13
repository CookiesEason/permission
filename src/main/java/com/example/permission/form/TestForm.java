package com.example.permission.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author CookiesEason
 * 2019/01/13 19:22
 */
@Data
public class TestForm {

    @NotBlank
    private String msg;

    @NotNull
    private Integer id;

//    @NotEmpty
//    private List<String> strings;

}
