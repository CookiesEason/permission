package com.example.permission.service;

import com.example.permission.common.RequestHolder;
import com.example.permission.dao.SysRoleAclMapper;
import com.example.permission.model.SysRoleAcl;
import com.example.permission.util.IPUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author CookiesEason
 * 2019/01/19 10:46
 */
@Service
public class SysRoleAclService {

    @Resource
    private SysRoleAclMapper sysRoleAclMapper;

    public void changeRoleAcls(Integer roleId, List<Integer> aclIds) {
        List<Integer> hasAclIds = sysRoleAclMapper.getAclIdListByRoleIdList(Lists.newArrayList(roleId));
        if (hasAclIds.size() == aclIds.size()) {
            Set<Integer> hasAclIdSet = Sets.newHashSet(hasAclIds);
            Set<Integer> aclIdSet = Sets.newHashSet(aclIds);
            hasAclIdSet.removeAll(aclIdSet);
            if (CollectionUtils.isEmpty(hasAclIdSet)) {
                return;
            }
        }
        updateRoleAcls(roleId, aclIds);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateRoleAcls(Integer roleId, List<Integer> aclIds) {
        sysRoleAclMapper.deleteByRoleId(roleId);
        if (CollectionUtils.isEmpty(aclIds)) {
            return;
        }
        List<SysRoleAcl> roleAclList = Lists.newArrayList();
        for (Integer aclId : aclIds) {
            SysRoleAcl sysRoleAcl = SysRoleAcl.builder()
                    .roleId(roleId).aclId(aclId)
                    .operator(RequestHolder.getUser().getUsername())
                    .operateIp(IPUtil.getRemoteIp(RequestHolder.getRequest()))
                    .operateTime(new Date()).build();
            roleAclList.add(sysRoleAcl);
        }
        sysRoleAclMapper.batchInsert(roleAclList);
    }

}
