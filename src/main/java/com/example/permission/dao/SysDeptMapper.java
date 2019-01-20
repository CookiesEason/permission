package com.example.permission.dao;

import com.example.permission.model.SysDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysDeptMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysDept record);

    int insertSelective(SysDept record);

    SysDept selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysDept record);

    int updateByPrimaryKey(SysDept record);

    List<SysDept> getAllDept();

    List<SysDept> getChildDeptListByLevel(@Param("level") String level);

    List<SysDept> getChildDeptListByTop(@Param("level") String level);

    void batchUpdateLevel(@Param("sysDepts") List<SysDept> sysDepts);

    int countByNameAndParentId(@Param("parentId")Integer parentId, @Param("name")String name, @Param("id")Integer id);

    int countByParentId(@Param("deptId") Integer deptId);

}