package com.example.permission.service;

import com.example.permission.dao.SysAclMapper;
import com.example.permission.dao.SysAclModuleMapper;
import com.example.permission.dao.SysDeptMapper;
import com.example.permission.dto.AclDto;
import com.example.permission.dto.AclModuleDto;
import com.example.permission.dto.DeptLevelDto;
import com.example.permission.model.SysAcl;
import com.example.permission.model.SysAclModule;
import com.example.permission.model.SysDept;
import com.example.permission.util.LevelUtil;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author CookiesEason
 * 2019/01/14 16:42
 */
@Service
public class SysTreeService {

    @Resource
    private SysDeptMapper sysDeptMapper;

    @Resource
    private SysAclModuleMapper aclModuleMapper;

    @Resource
    private SysCoreService sysCoreService;

    @Resource
    private SysAclMapper sysAclMapper;

    public List<DeptLevelDto> deptTree() {
        List<SysDept> sysDepts = sysDeptMapper.getAllDept();
        List<DeptLevelDto> dtos = Lists.newArrayList();
        for (SysDept dept : sysDepts) {
            DeptLevelDto deptLevelDto = DeptLevelDto.adapt(dept);
            dtos.add(deptLevelDto);
        }
        return deptListToTree(dtos);
    }

    public List<AclModuleDto> aclModuleTree() {
        List<SysAclModule> sysAclModules = aclModuleMapper.getAllAclModules();
        List<AclModuleDto> aclModuleDtos = Lists.newArrayList();
        for (SysAclModule aclModule : sysAclModules) {
            AclModuleDto dto = AclModuleDto.adapt(aclModule);
            aclModuleDtos.add(dto);
        }
        return aclModuleToTree(aclModuleDtos);
    }

    public List<AclModuleDto> roleTree(Integer roleId) {
        List<SysAcl> sysAclList = sysCoreService.getCurrentUserAcls();
        List<SysAcl> roleAclList = sysCoreService.getRoleAcls(roleId);
        List<SysAcl> aclList = sysAclMapper.getAll();
        Set<Integer> userAclIdSet = sysAclList.stream().map(SysAcl::getId).collect(Collectors.toSet());
        Set<Integer> roleAclSet = roleAclList.stream().map(SysAcl::getId).collect(Collectors.toSet());
        List<AclDto> aclDtoList = Lists.newArrayList();
        for (SysAcl sysAcl : aclList) {
            AclDto aclDto = AclDto.adapt(sysAcl);
            if (userAclIdSet.contains(sysAcl.getId())) {
                aclDto.setHasAcl(true);
            }
            if (roleAclSet.contains(sysAcl.getId())) {
                aclDto.setChecked(true);
            }
            aclDtoList.add(aclDto);
        }
        return aclListToTree(aclDtoList);
    }

    private List<DeptLevelDto> deptListToTree(List<DeptLevelDto> deptLevelList) {
        if (CollectionUtils.isEmpty(deptLevelList)) {
            return Lists.newArrayList();
        }
        Multimap<String, DeptLevelDto> levelDtoMultimap = ArrayListMultimap.create();
        List<DeptLevelDto> rootList = Lists.newArrayList();
        for (DeptLevelDto deptLevelDto : deptLevelList) {
            levelDtoMultimap.put(deptLevelDto.getLevel(), deptLevelDto);
            if (LevelUtil.ROOT.equals(deptLevelDto.getLevel())) {
                rootList.add(deptLevelDto);
            }
        }
        rootList.sort(Comparator.comparingInt(SysDept::getSeq));
        transformDeptTree(rootList, LevelUtil.ROOT, levelDtoMultimap);
        return rootList;
    }

    private List<AclModuleDto> aclModuleToTree(List<AclModuleDto> aclModuleDtoList) {
        if (CollectionUtils.isEmpty(aclModuleDtoList)) {
            return Lists.newArrayList();
        }
        Multimap<String, AclModuleDto> levelDtoMultimap = ArrayListMultimap.create();
        List<AclModuleDto> rootList = Lists.newArrayList();
        for (AclModuleDto aclModuleDto : aclModuleDtoList) {
            levelDtoMultimap.put(aclModuleDto.getLevel(), aclModuleDto);
            if (LevelUtil.ROOT.equals(aclModuleDto.getLevel())) {
                rootList.add(aclModuleDto);
            }
        }
        rootList.sort(Comparator.comparingInt(SysAclModule::getSeq));
        transformAclModuleTree(rootList, LevelUtil.ROOT, levelDtoMultimap);
        return rootList;
    }


    private void transformDeptTree(List<DeptLevelDto> deptLevelDtoList, String level,
                                  Multimap<String, DeptLevelDto> levelDtoMultimap) {
        for (DeptLevelDto deptLevelDto : deptLevelDtoList) {
            String nextLevel = LevelUtil.calculateLevel(level, deptLevelDto.getId());
            List<DeptLevelDto> temp = (List<DeptLevelDto>) levelDtoMultimap.get(nextLevel);
            if (temp != null) {
                temp.sort(Comparator.comparingInt(SysDept::getSeq));
                deptLevelDto.setDepts(temp);
                transformDeptTree(temp, nextLevel, levelDtoMultimap);
            }
        }
    }

    private void transformAclModuleTree(List<AclModuleDto> aclModuleDtoList, String level,
                                   Multimap<String, AclModuleDto> levelDtoMultimap) {
        for (AclModuleDto aclModuleDto : aclModuleDtoList) {
            String nextLevel = LevelUtil.calculateLevel(level, aclModuleDto.getId());
            List<AclModuleDto> temp = (List<AclModuleDto>) levelDtoMultimap.get(nextLevel);
            if (temp != null) {
                temp.sort(Comparator.comparingInt(SysAclModule::getSeq));
                aclModuleDto.setAclModules(temp);
                transformAclModuleTree(temp, nextLevel, levelDtoMultimap);
            }
        }
    }

    private List<AclModuleDto> aclListToTree(List<AclDto> aclDtoList) {
        if (CollectionUtils.isEmpty(aclDtoList)) {
            return Lists.newArrayList();
        }
        List<AclModuleDto> aclModuleDtoLists = aclModuleTree();
        Multimap<Integer, AclDto> moduleIdAclMap = ArrayListMultimap.create();
        for (AclDto dto : aclDtoList) {
            if (dto.getStatus() == 1) {
                moduleIdAclMap.put(dto.getAclModuleId(), dto);
            }
        }
        bindAclsWithOrder(aclModuleDtoLists, moduleIdAclMap);
        return aclModuleDtoLists;
    }

    private void bindAclsWithOrder(List<AclModuleDto> aclModuleDtoList,
                                   Multimap<Integer, AclDto> moduleIdAclMap) {
        if (CollectionUtils.isEmpty(aclModuleDtoList)) {
            return;
        }
        for (AclModuleDto moduleDto : aclModuleDtoList) {
            List<AclDto> aclDtoList = (List<AclDto>) moduleIdAclMap.get(moduleDto.getId());
            if (!CollectionUtils.isEmpty(aclDtoList)) {
                aclDtoList.sort(Comparator.comparingInt(SysAcl::getSeq));
                moduleDto.setAcls(aclDtoList);
            }
            bindAclsWithOrder(moduleDto.getAclModules(), moduleIdAclMap);
        }
    }

}
