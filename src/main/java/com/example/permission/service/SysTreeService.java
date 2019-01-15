package com.example.permission.service;

import com.example.permission.dao.SysDeptMapper;
import com.example.permission.dto.DeptLevelDto;
import com.example.permission.model.SysDept;
import com.example.permission.util.LevelUtil;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;

/**
 * @author CookiesEason
 * 2019/01/14 16:42
 */
@Service
public class SysTreeService {

    @Resource
    private SysDeptMapper sysDeptMapper;

    public List<DeptLevelDto> deptTree() {
        List<SysDept> sysDepts = sysDeptMapper.getAllDept();
        List<DeptLevelDto> dtos = Lists.newArrayList();
        for (SysDept dept : sysDepts) {
            DeptLevelDto deptLevelDto = DeptLevelDto.adapt(dept);
            dtos.add(deptLevelDto);
        }
        return deptListToTree(dtos);
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

}
