package com.example.permission.service;

import com.example.permission.common.LogType;
import com.example.permission.common.RequestHolder;
import com.example.permission.dao.SysLogMapper;
import com.example.permission.dao.SysRoleUserMapper;
import com.example.permission.dao.SysUserMapper;
import com.example.permission.model.SysLogWithBLOBs;
import com.example.permission.model.SysRoleUser;
import com.example.permission.model.SysUser;
import com.example.permission.util.IPUtil;
import com.example.permission.util.JsonUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Resource
    private SysLogMapper sysLogMapper;

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
        saveRoleUserLog(roleId, oldUserIdList, userIdList);
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


    private void saveRoleUserLog(int roleId, List<Integer> before, List<Integer> after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ROLE_USER);
        sysLog.setTargetId(roleId);
        sysLog.setOldValue(before == null ? "" : JsonUtil.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonUtil.obj2String(after));
        sysLog.setOperator(RequestHolder.getUser().getUsername());
        sysLog.setOperateIp(IPUtil.getRemoteIp(RequestHolder.getRequest()));
        sysLog.setOperateTime(new Date());
        sysLog.setStatus(1);
        sysLogMapper.insertSelective(sysLog);
    }

}
