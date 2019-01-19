package com.example.permission.dto;

import com.example.permission.model.SysAcl;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

/**
 * @author CookiesEason
 * 2019/01/19 9:37
 */
@Getter
@Setter
@ToString
public class AclDto extends SysAcl {

    private boolean checked = false;

    private boolean hasAcl = false;

    public static AclDto adapt(SysAcl acl) {
        AclDto aclDto = new AclDto();
        BeanUtils.copyProperties(acl, aclDto);
        return aclDto;
    }

}
