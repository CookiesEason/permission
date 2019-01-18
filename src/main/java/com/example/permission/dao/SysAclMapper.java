package com.example.permission.dao;

import com.example.permission.form.PageQuery;
import com.example.permission.model.SysAcl;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysAclMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAcl record);

    int insertSelective(SysAcl record);

    SysAcl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAcl record);

    int updateByPrimaryKey(SysAcl record);

    int countByModuleIdAndName(@Param("aclModuleId") Integer aclModuleId, @Param("name") String name,
                               @Param("id") Integer aclId);

    int countByAclModuleId(@Param("aclModuleId") Integer aclModuleId);

    List<SysAcl> getPageByAclModuleId(@Param("aclModuleId")Integer aclModuleId,@Param("page")PageQuery pageQuery);
}