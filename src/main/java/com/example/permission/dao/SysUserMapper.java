package com.example.permission.dao;

import com.example.permission.model.SysUser;
import org.apache.ibatis.annotations.Param;

public interface SysUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    SysUser findByKeyword(@Param("keyword") String keyword);

    int countByEmail(@Param("email") String email, @Param("userId") Integer userId);

    int countByTelephone(@Param("phone") String phone, @Param("userId") Integer userId);
}