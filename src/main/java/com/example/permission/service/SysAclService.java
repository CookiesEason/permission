package com.example.permission.service;

import com.example.permission.common.RequestHolder;
import com.example.permission.dao.SysAclMapper;
import com.example.permission.exception.ParamException;
import com.example.permission.exception.PermissionException;
import com.example.permission.form.AclParam;
import com.example.permission.form.PageQuery;
import com.example.permission.model.SysAcl;
import com.example.permission.util.IPUtil;
import com.example.permission.util.ValidatorUtil;
import com.example.permission.vo.PageResult;
import com.google.common.base.Preconditions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author CookiesEason
 * 2019/01/18 14:15
 */
@Service
public class SysAclService {

    @Resource
    private SysAclMapper sysAclMapper;

    public void save(AclParam aclParam) {
        ValidatorUtil.check(aclParam);
        if (checkExist(aclParam.getAclModuleId(), aclParam.getName(), aclParam.getId())) {
            throw new ParamException("当前权限模块下存在相同权限名称");
        }
        SysAcl sysAcl = SysAcl.builder().name(aclParam.getName()).aclModuleId(aclParam.getAclModuleId())
                .url(aclParam.getUrl()).type(aclParam.getType()).status(aclParam.getStatus())
                .seq(aclParam.getSeq()).remark(aclParam.getRemark()).build();
        sysAcl.setCode(generateCode());
        sysAcl.setOperator(RequestHolder.getUser().getUsername());
        sysAcl.setOperateIp(IPUtil.getRemoteIp(RequestHolder.getRequest()));
        sysAcl.setOperateTime(new Date());
        sysAclMapper.insertSelective(sysAcl);
    }

    public void update(AclParam aclParam) {
        ValidatorUtil.check(aclParam);
        if (checkExist(aclParam.getAclModuleId(), aclParam.getName(), aclParam.getId())) {
            throw new ParamException("当前权限模块下存在相同权限名称");
        }
        SysAcl old = sysAclMapper.selectByPrimaryKey(aclParam.getId());
        Preconditions.checkNotNull(old, "更新权限不存在");
        SysAcl sysAcl = SysAcl.builder().id(aclParam.getId()).name(aclParam.getName()).aclModuleId(aclParam.getAclModuleId())
                .url(aclParam.getUrl()).type(aclParam.getType()).status(aclParam.getStatus())
                .seq(aclParam.getSeq()).remark(aclParam.getRemark()).build();
        sysAcl.setCode(generateCode());
        sysAcl.setOperator(RequestHolder.getUser().getUsername());
        sysAcl.setOperateIp(IPUtil.getRemoteIp(RequestHolder.getRequest()));
        sysAcl.setOperateTime(new Date());
        sysAclMapper.updateByPrimaryKeySelective(sysAcl);
    }

    public PageResult<SysAcl> getPageByAclModuleId(Integer aclModuleId, PageQuery pageQuery) {
        ValidatorUtil.check(pageQuery);
        int count = sysAclMapper.countByAclModuleId(aclModuleId);
        if (count > 0) {
            List<SysAcl> sysAcls = sysAclMapper.getPageByAclModuleId(aclModuleId, pageQuery);
            return PageResult.<SysAcl>builder().data(sysAcls).total(count).build();
        }
        return PageResult.<SysAcl>builder().build();
    }

    private boolean checkExist(Integer aclModuleId, String name, Integer id) {
        return sysAclMapper.countByModuleIdAndName(aclModuleId, name, id) > 0;
    }

    private String generateCode() {
        Random random = new Random();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(new Date()) + "_" + random.nextInt(100);
    }


}
