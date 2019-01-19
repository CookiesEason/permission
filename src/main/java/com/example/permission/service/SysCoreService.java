package com.example.permission.service;

import com.example.permission.common.RequestHolder;
import com.example.permission.dao.SysAclMapper;
import com.example.permission.dao.SysRoleAclMapper;
import com.example.permission.dao.SysRoleUserMapper;
import com.example.permission.model.SysAcl;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author CookiesEason
 * 2019/01/19 9:42
 */
@Service
public class SysCoreService {

    @Resource
    private SysAclMapper sysAclMapper;

    @Resource
    private SysRoleUserMapper sysRoleUserMapper;

    @Resource
    private SysRoleAclMapper sysRoleAclMapper;

    public List<SysAcl> getCurrentUserAcls() {
        int userId = RequestHolder.getUser().getId();
        return getUserAcls(userId);
    }

    public List<SysAcl> getRoleAcls(Integer roleId) {
        List<Integer> aclIdList = sysRoleAclMapper
                .getAclIdListByRoleIdList(Lists.newArrayList(roleId));
        if (CollectionUtils.isEmpty(aclIdList)) {
            return Lists.newArrayList();
        }
        return sysAclMapper.getByIdList(aclIdList);
    }

    private List<SysAcl> getUserAcls(Integer userId) {
        if (isSuperAdmin()) {
            sysAclMapper.getAll();
        }
        List<Integer> userRoleIdList = sysRoleUserMapper.getRoleByUserId(userId);
        if (CollectionUtils.isEmpty(userRoleIdList)) {
            return Lists.newArrayList();
        }
        List<Integer> userAclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(userRoleIdList);
        if (CollectionUtils.isEmpty(userAclIdList)) {
            return Lists.newArrayList();
        }
        return sysAclMapper.getByIdList(userAclIdList);
    }

    private boolean isSuperAdmin() {
        return true;
    }


}
