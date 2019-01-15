package com.example.permission.form;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

/**
 * @author CookiesEason
 * 2019/01/15 16:51
 */
@Getter
@Setter
public class UserParam {

    private Integer id;

    @NotBlank(message = "用户名不能为空")
    @Length(max = 20, message = "用户名长度为0-20位")
    private String name;

    @NotBlank(message = "联系方式不能为空")
    @Length(min = 1, max = 13, message = "联系方式13位以内")
    private String telephone;

    @NotBlank(message = "邮箱不能为空")
    @Length(min = 5, max = 20,message = "用户名长度为0-20位")
    @Email(message = "邮箱格式不正确")
    private String mail;

    @NotNull(message = "必须提供用户所在部门")
    private Integer deptId;

    @NotNull(message = "必须指定用户状态")
    @Min(value = 0, message = "用户状态不合法")
    @Max(value = 2, message = "用户状态不合法")
    private Integer status;

    @Length(max = 200,message = "备注长度200字内")
    private String remark;

}
