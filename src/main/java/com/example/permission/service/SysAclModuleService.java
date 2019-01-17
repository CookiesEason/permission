package com.example.permission.service;

import com.example.permission.common.RequestHolder;
import com.example.permission.dao.SysAclModuleMapper;
import com.example.permission.exception.PermissionException;
import com.example.permission.form.AclModuleParam;
import com.example.permission.model.SysAclModule;
import com.example.permission.util.IPUtil;
import com.example.permission.util.LevelUtil;
import com.example.permission.util.ValidatorUtil;
import com.google.common.base.Preconditions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author CookiesEason
 * 2019/01/17 18:38
 */
@Service
public class SysAclModuleService {

    @Resource
    private SysAclModuleMapper sysAclModuleMapper;

    public void save(AclModuleParam aclModuleParam) {
        ValidatorUtil.check(aclModuleParam);
        if (checkExist(aclModuleParam.getParentId(), aclModuleParam.getName(), aclModuleParam.getId())) {
            throw new PermissionException("权限模块有相同的相同的权限名称");
        }
        SysAclModule sysAclModule = SysAclModule.builder().name(aclModuleParam.getName())
                .parentId(aclModuleParam.getParentId()).seq(aclModuleParam.getSeq())
                .status(aclModuleParam.getStatus()).remark(aclModuleParam.getRemark()).build();
        sysAclModule.setLevel(LevelUtil.calculateLevel(getLevel(aclModuleParam.getParentId()),
                aclModuleParam.getParentId() ));
        sysAclModule.setOperator("Test");
        sysAclModule.setOperateIp("127.0.0.1");
        sysAclModule.setName(RequestHolder.getUser().getUsername());
        sysAclModule.setOperateIp(IPUtil.getRemoteIp(RequestHolder.getRequest()));
        sysAclModule.setOperateTime(new Date());
        sysAclModuleMapper.insertSelective(sysAclModule);
    }

    public void update(AclModuleParam aclModuleParam) {
        ValidatorUtil.check(aclModuleParam);
        if (checkExist(aclModuleParam.getParentId(), aclModuleParam.getName(), aclModuleParam.getId())) {
            throw new PermissionException("权限模块有相同的相同的权限名称");
        }
        SysAclModule old = sysAclModuleMapper.selectByPrimaryKey(aclModuleParam.getId());
        Preconditions.checkNotNull(old, "更新权限模块不存在");
        SysAclModule sysAclModule = SysAclModule.builder().id(aclModuleParam.getId()).name(aclModuleParam.getName())
                .parentId(aclModuleParam.getParentId()).seq(aclModuleParam.getSeq())
                .status(aclModuleParam.getStatus()).remark(aclModuleParam.getRemark()).build();
        sysAclModule.setLevel(LevelUtil.calculateLevel(getLevel(aclModuleParam.getParentId()),
                aclModuleParam.getParentId() ));
//        sysAclModule.setOperator("Test");
//        sysAclModule.setOperateIp("127.0.0.1");
        sysAclModule.setName(RequestHolder.getUser().getUsername());
        sysAclModule.setOperateIp(IPUtil.getRemoteIp(RequestHolder.getRequest()));
        sysAclModule.setOperateTime(new Date());
        updateWithChild(old, sysAclModule);
    }

    private void updateWithChild(SysAclModule aclModule, SysAclModule newAclModule) {
        String newLevel = newAclModule.getLevel();
        String oldLevel = aclModule.getLevel();
        if (!newLevel.equals(oldLevel)) {
            List<SysAclModule> sysAclModuleList;
            if (aclModule.getParentId() == 0) {
                String level = LevelUtil.calculateLevel(String.valueOf(0), aclModule.getId());
                sysAclModuleList = sysAclModuleMapper.getChildAclModuleListByTop(level);
            } else {
                sysAclModuleList = sysAclModuleMapper.getChildAclModuleListByLevel(aclModule.getLevel());
            }
            if (sysAclModuleList != null && sysAclModuleList.size() > 0) {
                for (SysAclModule sysAclModule : sysAclModuleList) {
                    String level  = sysAclModule.getLevel();
                    if (level.indexOf(oldLevel) == 0) {
                        level = newLevel + level.substring(oldLevel.length());
                        sysAclModule.setLevel(level);
                    }
                }
                sysAclModuleMapper.batchUpdateLevel(sysAclModuleList);
            }
        }
        sysAclModuleMapper.updateByPrimaryKeySelective(newAclModule);
    }


    private boolean checkExist(Integer parentId, String aclName, Integer aclId) {
        return sysAclModuleMapper.countByNameAndParentId(parentId, aclName, aclId) > 0;
    }

    private String getLevel(Integer id) {
        SysAclModule sysAclModule = sysAclModuleMapper.selectByPrimaryKey(id);
        if (sysAclModule == null) {
            return null;
        }
        return sysAclModule.getLevel();
    }

}
