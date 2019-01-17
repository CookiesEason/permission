package com.example.permission.service;

import com.example.permission.common.RequestHolder;
import com.example.permission.dao.SysDeptMapper;
import com.example.permission.exception.ParamException;
import com.example.permission.form.DeptParam;
import com.example.permission.model.SysDept;
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
 * 2019/01/14 16:14
 */
@Service
public class SysDeptService {

    @Resource
    private SysDeptMapper sysDeptMapper;

    public void save(DeptParam deptParam) {
        ValidatorUtil.check(deptParam);
        if (checkExist(deptParam.getParentId(), deptParam.getName(), deptParam.getId())) {
            throw new ParamException("当前同一层级下存在相同名称的部门");
        }
        SysDept sysDept = SysDept.builder().name(deptParam.getName()).
                parentId(deptParam.getParentId()).seq(deptParam.getSeq()).remark(deptParam.getRemark())
                .build();
        sysDept.setLevel(LevelUtil.calculateLevel(getLevel(deptParam.getParentId()), deptParam.getParentId()));
        // TODO: 2019/01/15  暂时固定
        sysDept.setOperator(RequestHolder.getUser().getUsername());
        sysDept.setOperateIp(IPUtil.getRemoteIp(RequestHolder.getRequest()));
        sysDept.setOperateTime(new Date());
        sysDeptMapper.insertSelective(sysDept);
    }

    public void update(DeptParam deptParam) {
        ValidatorUtil.check(deptParam);
        if (checkExist(deptParam.getParentId(), deptParam.getName(), deptParam.getId())) {
            throw new ParamException("当前同一层级下存在相同名称的部门");
        }
        SysDept old = sysDeptMapper.selectByPrimaryKey(deptParam.getId());
        Preconditions.checkNotNull(old, "待更新部门不存在");
        SysDept sysDept = SysDept.builder().id(deptParam.getId()).name(deptParam.getName()).
                parentId(deptParam.getParentId()).seq(deptParam.getSeq()).remark(deptParam.getRemark())
                .build();
        sysDept.setLevel(LevelUtil.calculateLevel(getLevel(deptParam.getParentId()), sysDept.getParentId() ));
        // TODO: 2019/01/15  暂时固定
        sysDept.setOperator(RequestHolder.getUser().getUsername());
        sysDept.setOperateIp(IPUtil.getRemoteIp(RequestHolder.getRequest()));
        sysDept.setOperateTime(new Date());
        updateWithChild(old, sysDept);
    }

    private void updateWithChild(SysDept oldDept, SysDept newDept) {
        String newLevel = newDept.getLevel();
        String oldLevel = oldDept.getLevel();
        if (!newLevel.equals(oldLevel)) {
            List<SysDept> sysDepts;
            if (oldDept.getParentId() == 0) {
                String level = LevelUtil.calculateLevel(String.valueOf(0), oldDept.getId());
                sysDepts = sysDeptMapper.getChildDeptListByTop(level);
            } else {
                sysDepts = sysDeptMapper.getChildDeptListByLevel(oldDept.getLevel());
            }
            if (sysDepts != null && sysDepts.size() > 0) {
                for (SysDept sysDept : sysDepts) {
                    String level  = sysDept.getLevel();
                    if (level.indexOf(oldLevel) == 0) {
                        level = newLevel + level.substring(oldLevel.length());
                        sysDept.setLevel(level);
                    }
                }
                sysDeptMapper.batchUpdateLevel(sysDepts);
            }
        }
        sysDeptMapper.updateByPrimaryKey(newDept);
    }

    private boolean checkExist(Integer parentId, String deptName, Integer deptId) {
        return sysDeptMapper.countByNameAndParentId(parentId, deptName, deptId) > 0;
    }

    private String getLevel(Integer id) {
        SysDept sysDept = sysDeptMapper.selectByPrimaryKey(id);
        if (sysDept == null) {
            return null;
        }
        return sysDept.getLevel();
    }

}
