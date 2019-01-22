package com.example.permission.service;

import com.example.permission.common.LogType;
import com.example.permission.common.RequestHolder;
import com.example.permission.dao.*;
import com.example.permission.dto.LogDto;
import com.example.permission.exception.ParamException;
import com.example.permission.form.PageQuery;
import com.example.permission.form.SearchLogParam;
import com.example.permission.model.*;
import com.example.permission.util.IPUtil;
import com.example.permission.util.JsonUtil;
import com.example.permission.util.ValidatorUtil;
import com.example.permission.vo.PageResult;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author CookiesEason
 * 2019/01/22 10:56
 */
@Service
public class SysLogService {

    @Resource
    private SysLogMapper sysLogMapper;

    @Resource
    private SysDeptMapper sysDeptMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysAclModuleMapper sysAclModuleMapper;

    @Resource
    private SysAclMapper sysAclMapper;

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRoleAclService sysRoleAclService;

    @Autowired
    private SysRoleUserService sysRoleUserService;

    public PageResult<SysLogWithBLOBs> getSysLogPageResult(SearchLogParam searchLogParam, PageQuery pageQuery) {
        ValidatorUtil.check(pageQuery);
        LogDto logDto = new LogDto();
        logDto.setType(searchLogParam.getType());
        if (StringUtils.isNotBlank(searchLogParam.getBeforeSeq())) {
            logDto.setBeforeSeg("%" + searchLogParam.getBeforeSeq() + "%");
        }
        if (StringUtils.isNotBlank(searchLogParam.getAfterSeq())) {
            logDto.setAfterSeg("%" + searchLogParam.getAfterSeq() + "%");
        }
        if (StringUtils.isNotBlank(searchLogParam.getOperator())) {
            logDto.setOperator("%" + searchLogParam.getOperator() + "%");
        }
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (StringUtils.isNotBlank(searchLogParam.getFromTime())) {
                logDto.setFromTime(simpleDateFormat.parse(searchLogParam.getFromTime()));
            }
            if (StringUtils.isNotBlank(searchLogParam.getToTime())) {
                logDto.setToTime(simpleDateFormat.parse(searchLogParam.getToTime()));
            }
        } catch (ParseException e) {
            throw new ParamException("传入的日期格式出错，格式为yyyy-MM-dd HH:mm:ss");
        }
        int count = sysLogMapper.countByLogDto(logDto);
        if ( count > 0) {
            List<SysLogWithBLOBs> sysLogList = sysLogMapper.getPageByLogDto(logDto, pageQuery);
            return PageResult.<SysLogWithBLOBs>builder().data(sysLogList).total(count).build();
        }
        return PageResult.<SysLogWithBLOBs>builder().build();
    }

    public void recover(Integer id) {
        SysLogWithBLOBs sysLogWithBLOBs = sysLogMapper.selectByPrimaryKey(id);
        Preconditions.checkNotNull(sysLogWithBLOBs, "待还原的记录不存在");
        switch (sysLogWithBLOBs.getType()) {
            case LogType.TYPE_DEPT :
                SysDept beforeDept = sysDeptMapper.selectByPrimaryKey(sysLogWithBLOBs.getTargetId());
                Preconditions.checkNotNull(beforeDept, "待还原的部门已不存在");
                if (StringUtils.isBlank(sysLogWithBLOBs.getNewValue()) ||
                        StringUtils.isBlank(sysLogWithBLOBs.getOldValue())){
                    throw new ParamException("新增和删除操作不还原");
                }
                SysDept afterDept = JsonUtil.string2Object(sysLogWithBLOBs.getOldValue(), new TypeReference<SysDept>(){});
                afterDept.setOperator(RequestHolder.getUser().getUsername());
                afterDept.setOperateIp(IPUtil.getRemoteIp(RequestHolder.getRequest()));
                afterDept.setOperateTime(new Date());
                sysDeptMapper.updateByPrimaryKeySelective(afterDept);
                saveDeptLog(beforeDept, afterDept);
                break;
            case LogType.TYPE_USER:
                SysUser beforeUser = sysUserMapper.selectByPrimaryKey(sysLogWithBLOBs.getTargetId());
                Preconditions.checkNotNull(beforeUser, "待还原的用户已不存在");
                if (StringUtils.isBlank(sysLogWithBLOBs.getNewValue()) ||
                        StringUtils.isBlank(sysLogWithBLOBs.getOldValue())){
                    throw new ParamException("新增和删除操作不还原");
                }
                SysUser afterUser = JsonUtil.string2Object(sysLogWithBLOBs.getOldValue(), new TypeReference<SysUser>(){});
                afterUser.setOperator(RequestHolder.getUser().getUsername());
                afterUser.setOperateIp(IPUtil.getRemoteIp(RequestHolder.getRequest()));
                afterUser.setOperateTime(new Date());
                sysUserMapper.updateByPrimaryKeySelective(afterUser);
                saveUserLog(beforeUser, afterUser);
                break;
            case LogType.TYPE_ACL_MODULE:
                SysAclModule beforeModule= sysAclModuleMapper.selectByPrimaryKey(sysLogWithBLOBs.getTargetId());
                Preconditions.checkNotNull(beforeModule, "待还原的权限模块已不存在");
                if (StringUtils.isBlank(sysLogWithBLOBs.getNewValue()) ||
                        StringUtils.isBlank(sysLogWithBLOBs.getOldValue())){
                    throw new ParamException("新增和删除操作不还原");
                }
                SysAclModule afterModule = JsonUtil.string2Object(sysLogWithBLOBs.getOldValue(), new TypeReference<SysAclModule>(){});
                afterModule.setOperator(RequestHolder.getUser().getUsername());
                afterModule.setOperateIp(IPUtil.getRemoteIp(RequestHolder.getRequest()));
                afterModule.setOperateTime(new Date());
                sysAclModuleMapper.updateByPrimaryKeySelective(afterModule);
                saveAclmoduleLog(beforeModule, afterModule);
                break;
            case LogType.TYPE_ACL:
                SysAcl beforeAcl= sysAclMapper.selectByPrimaryKey(sysLogWithBLOBs.getTargetId());
                Preconditions.checkNotNull(beforeAcl, "待还原的权限已不存在");
                if (StringUtils.isBlank(sysLogWithBLOBs.getNewValue()) ||
                        StringUtils.isBlank(sysLogWithBLOBs.getOldValue())){
                    throw new ParamException("新增和删除操作不还原");
                }
                SysAcl afterAcl = JsonUtil.string2Object(sysLogWithBLOBs.getOldValue(), new TypeReference<SysAcl>(){});
                afterAcl.setOperator(RequestHolder.getUser().getUsername());
                afterAcl.setOperateIp(IPUtil.getRemoteIp(RequestHolder.getRequest()));
                afterAcl.setOperateTime(new Date());
                sysAclMapper.updateByPrimaryKeySelective(afterAcl);
                saveAclLog(beforeAcl, afterAcl);
                break;
            case LogType.TYPE_ROLE:
                SysRole beforeRole= sysRoleMapper.selectByPrimaryKey(sysLogWithBLOBs.getTargetId());
                Preconditions.checkNotNull(beforeRole, "待还原的角色已不存在");
                if (StringUtils.isBlank(sysLogWithBLOBs.getNewValue()) ||
                        StringUtils.isBlank(sysLogWithBLOBs.getOldValue())){
                    throw new ParamException("新增和删除操作不还原");
                }
                SysRole afterRole= JsonUtil.string2Object(sysLogWithBLOBs.getOldValue(), new TypeReference<SysRole>(){});
                afterRole.setOperator(RequestHolder.getUser().getUsername());
                afterRole.setOperateIp(IPUtil.getRemoteIp(RequestHolder.getRequest()));
                afterRole.setOperateTime(new Date());
                sysRoleMapper.updateByPrimaryKeySelective(afterRole);
                saveRoleLog(beforeRole, afterRole);
                break;
            case LogType.TYPE_ROLE_ACL:
                SysRole aclRole= sysRoleMapper.selectByPrimaryKey(sysLogWithBLOBs.getTargetId());
                Preconditions.checkNotNull(aclRole, "待还原的角色已不存在");
                sysRoleAclService.changeRoleAcls(sysLogWithBLOBs.getTargetId(),
                        JsonUtil.string2Object(sysLogWithBLOBs.getOldValue(),  new TypeReference<List<Integer>>(){}));
                break;
            case LogType.TYPE_ROLE_USER:
                SysRole userRole= sysRoleMapper.selectByPrimaryKey(sysLogWithBLOBs.getTargetId());
                Preconditions.checkNotNull(userRole, "待还原的角色已不存在");
                sysRoleUserService.changeUsers(sysLogWithBLOBs.getTargetId(),
                        JsonUtil.string2Object(sysLogWithBLOBs.getOldValue(),  new TypeReference<List<Integer>>(){}));
                break;
            default:
        }
    }

    public void saveDeptLog(SysDept before, SysDept after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_DEPT);
        sysLog.setTargetId(after == null ? before.getId() : after.getId());
        sysLog.setOldValue(before == null ? "" : JsonUtil.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonUtil.obj2String(after));
        save(sysLog);
    }

    public void saveUserLog(SysUser before, SysUser after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_USER);
        sysLog.setTargetId(after == null ? before.getId() : after.getId());
        sysLog.setOldValue(before == null ? "" : JsonUtil.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonUtil.obj2String(after));
        save(sysLog);
    }

    public void saveAclmoduleLog(SysAclModule before, SysAclModule after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ACL_MODULE);
        sysLog.setTargetId(after == null ? before.getId() : after.getId());
        sysLog.setOldValue(before == null ? "" : JsonUtil.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonUtil.obj2String(after));
        save(sysLog);
    }

    public void saveAclLog(SysAcl before, SysAcl after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ACL);
        sysLog.setTargetId(after == null ? before.getId() : after.getId());
        sysLog.setOldValue(before == null ? "" : JsonUtil.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonUtil.obj2String(after));
        save(sysLog);
    }

    public void saveRoleLog(SysRole before, SysRole after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ROLE);
        sysLog.setTargetId(after == null ? before.getId() : after.getId());
        sysLog.setOldValue(before == null ? "" : JsonUtil.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonUtil.obj2String(after));
        save(sysLog);
    }

    private void save(SysLogWithBLOBs sysLog) {
        sysLog.setOperator(RequestHolder.getUser().getUsername());
        sysLog.setOperateIp(IPUtil.getRemoteIp(RequestHolder.getRequest()));
        sysLog.setOperateTime(new Date());
        sysLog.setStatus(1);
        sysLogMapper.insertSelective(sysLog);
    }

}
