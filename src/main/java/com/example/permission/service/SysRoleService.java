package com.example.permission.service;

import com.example.permission.common.RequestHolder;
import com.example.permission.dao.SysRoleMapper;
import com.example.permission.exception.ParamException;
import com.example.permission.form.RoleParam;
import com.example.permission.model.SysRole;
import com.example.permission.util.IPUtil;
import com.example.permission.util.ValidatorUtil;
import com.google.common.base.Preconditions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author CookiesEason
 * 2019/01/18 21:12
 */
@Service
public class SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    public void save(RoleParam roleParam) {
        ValidatorUtil.check(roleParam);
        if (checkExistName(roleParam.getName(), roleParam.getId())) {
            throw new ParamException("角色名称已经存在");
        }
        SysRole sysRole = SysRole.builder().name(roleParam.getName()).status(roleParam.getStatus())
                .remark(roleParam.getRemark()).type(roleParam.getType()).build();
        sysRole.setOperator(RequestHolder.getUser().getUsername());
        sysRole.setOperateIp(IPUtil.getRemoteIp(RequestHolder.getRequest()));
        sysRole.setOperateTime(new Date());
        sysRoleMapper.insertSelective(sysRole);
    }

    public void update(RoleParam roleParam) {
        ValidatorUtil.check(roleParam);
        if (checkExistName(roleParam.getName(), roleParam.getId())) {
            throw new ParamException("角色名称已经存在");
        }
        SysRole old = sysRoleMapper.selectByPrimaryKey(roleParam.getId());
        Preconditions.checkNotNull(old, "待更新角色不存在");
        SysRole sysRole = SysRole.builder().id(roleParam.getId()).name(roleParam.getName()).status(roleParam.getStatus())
                .remark(roleParam.getRemark()).type(roleParam.getType()).build();
        sysRole.setOperator(RequestHolder.getUser().getUsername());
        sysRole.setOperateIp(IPUtil.getRemoteIp(RequestHolder.getRequest()));
        sysRole.setOperateTime(new Date());
        sysRoleMapper.updateByPrimaryKeySelective(sysRole);
    }

    public List<SysRole> getAll() {
        return sysRoleMapper.getAll();
    }

    private boolean checkExistName(String name, Integer id) {
        return sysRoleMapper.countByName(name, id) > 0;
    }

}
