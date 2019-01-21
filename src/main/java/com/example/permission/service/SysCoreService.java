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
import java.util.Set;
import java.util.stream.Collectors;

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

    public boolean hasUrlAcl(String url) {
        if (isSuperAdmin()) {
            return true;
        }
        List<SysAcl> sysAcls = sysAclMapper.getByUrl(url);
        if (CollectionUtils.isEmpty(sysAcls)) {
            return true;
        }
        List<SysAcl> userAcls = getCurrentUserAcls();
        Set<Integer> userIdSet = userAcls.stream().map(SysAcl::getId).collect(Collectors.toSet());
        boolean hasValidAcl = false;
        for (SysAcl sysAcl : sysAcls) {
            if (sysAcl == null || sysAcl.getStatus() != 1) {
                continue;
            }
            hasValidAcl = true;
            if (userIdSet.contains(sysAcl.getId())) {
                return true;
            }
        }
        if (!hasValidAcl) {
            return true;
        }
        return false;
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
        return RequestHolder.getUser().getUsername().contains("admin");
    }


}
