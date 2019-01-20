package com.example.permission.service;

import com.example.permission.common.RequestHolder;
import com.example.permission.dao.SysRoleUserMapper;
import com.example.permission.dao.SysUserMapper;
import com.example.permission.model.SysRoleUser;
import com.example.permission.model.SysUser;
import com.example.permission.util.IPUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author CookiesEason
 * 2019/01/20 10:29
 */
@Service
public class SysRoleUserService {

    @Resource
    private SysRoleUserMapper sysRoleUserMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    public List<SysUser> getListByRoleId(Integer roleId) {
        List<Integer> userIdList = sysRoleUserMapper.getUserIdListByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIdList)) {
            return Lists.newArrayList();
        }
        return sysUserMapper.getByIdList(userIdList);
    }

    public void changeUsers(Integer roleId, List<Integer> userIdList) {
        List<Integer> oldUserIdList = sysRoleUserMapper.getUserIdListByRoleId(roleId);
        if (oldUserIdList.size() == userIdList.size()) {
            Set<Integer> oldUserIdSet = Sets.newHashSet();
            Set<Integer> userIdSet = Sets.newHashSet(userIdList);
            oldUserIdSet.removeAll(userIdSet);
            if (CollectionUtils.isEmpty(oldUserIdSet)) {
                return;
            }
        }
        updateRoleUsers(roleId, userIdList);
    }

    private void updateRoleUsers(Integer roleId, List<Integer> userIdList) {
        sysRoleUserMapper.deleteByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIdList)) {
            return;
        }
        List<SysRoleUser> roleUsers = Lists.newArrayList();
        for (Integer id : userIdList) {
            SysRoleUser sysRoleUser = SysRoleUser.builder().userId(id).roleId(roleId)
                    .operator(RequestHolder.getUser().getUsername())
                    .operateIp(IPUtil.getRemoteIp(RequestHolder.getRequest()))
                    .operateTime(new Date()).build();
            roleUsers.add(sysRoleUser);
        }
        sysRoleUserMapper.batchInsert(roleUsers);
    }


}
