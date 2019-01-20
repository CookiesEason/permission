package com.example.permission.dao;

import com.example.permission.model.SysAclModule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysAclModuleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAclModule record);

    int insertSelective(SysAclModule record);

    SysAclModule selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAclModule record);

    int updateByPrimaryKey(SysAclModule record);

    List<SysAclModule> getAllAclModules();

    List<SysAclModule> getChildAclModuleListByLevel(@Param("level") String level);

    List<SysAclModule> getChildAclModuleListByTop(@Param("level") String level);

    void batchUpdateLevel(@Param("sysAclModules") List<SysAclModule> sysAclModules);

    int countByNameAndParentId(@Param("parentId")Integer parentId, @Param("name")String name, @Param("id")Integer id);

    int countByParentId(@Param("aclModuleId") Integer aclModuleId);

}