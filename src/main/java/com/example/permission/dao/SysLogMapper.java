package com.example.permission.dao;

import com.example.permission.dto.LogDto;
import com.example.permission.form.PageQuery;
import com.example.permission.model.SysLog;
import com.example.permission.model.SysLogWithBLOBs;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysLogWithBLOBs record);

    int insertSelective(SysLogWithBLOBs record);

    SysLogWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysLogWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(SysLogWithBLOBs record);

    int updateByPrimaryKey(SysLog record);

    int countByLogDto(@Param("dto") LogDto logDto);

    List<SysLogWithBLOBs> getPageByLogDto(@Param("dto") LogDto logDto,
                                 @Param("page") PageQuery pageQuery);

}