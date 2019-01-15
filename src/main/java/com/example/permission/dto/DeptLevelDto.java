package com.example.permission.dto;

import com.example.permission.model.SysDept;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @author CookiesEason
 * 2019/01/14 16:39
 */
@Getter
@Setter
@ToString
public class DeptLevelDto extends SysDept {

    private List<DeptLevelDto> depts = Lists.newArrayList();

    public static DeptLevelDto adapt(SysDept sysDept) {
        DeptLevelDto dto = new DeptLevelDto();
        BeanUtils.copyProperties(sysDept, dto);
        return dto;
    }

}
