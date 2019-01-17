package com.example.permission.dto;

import com.example.permission.model.SysAclModule;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @author CookiesEason
 * 2019/01/17 22:12
 */
@Setter
@Getter
@ToString
public class AclModuleDto extends SysAclModule {

    private List<AclModuleDto> aclModules = Lists.newArrayList();

    public static AclModuleDto adapt(SysAclModule sysAclModule) {
        AclModuleDto aclModuleDto = new AclModuleDto();
        BeanUtils.copyProperties(sysAclModule, aclModuleDto);
        return aclModuleDto;
    }

}
