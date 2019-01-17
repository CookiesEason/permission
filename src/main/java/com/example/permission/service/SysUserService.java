package com.example.permission.service;

import com.example.permission.dao.SysUserMapper;
import com.example.permission.exception.PermissionException;
import com.example.permission.form.PageQuery;
import com.example.permission.form.UserParam;
import com.example.permission.model.SysUser;
import com.example.permission.util.MD5Util;
import com.example.permission.util.PasswordUtil;
import com.example.permission.util.ResultVOUtil;
import com.example.permission.util.ValidatorUtil;
import com.example.permission.vo.PageResult;
import com.example.permission.vo.ResultVO;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @author CookiesEason
 * 2019/01/15 16:58
 */
@Service
public class SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    public void save(UserParam userParam) {
        check(userParam);
        String password = PasswordUtil.generate();
        // TODO: 2019/01/15  方便测试固定password
        password = "123456789a";
        password = MD5Util.encrypt(password);
        SysUser sysUser = SysUser.builder().deptId(userParam.getDeptId()).mail(userParam.getMail())
                .telephone(userParam.getTelephone()).remark(userParam.getRemark()).username(userParam.getName())
                .password(password).status(userParam.getStatus())
                .build();
        sysUser.setOperator("System");
        sysUser.setOperateIp("127.0.0.1");
        sysUser.setOperateTime(new Date());
        // TODO: 2019/01/15 sendEmail
        sysUserMapper.insertSelective(sysUser);
    }


    public void update(UserParam userParam) {
        check(userParam);
        SysUser old = sysUserMapper.selectByPrimaryKey(userParam.getId());
        Preconditions.checkNotNull(old, "用户不存在");
        SysUser sysUser = SysUser.builder().id(userParam.getId())
                .deptId(userParam.getDeptId()).mail(userParam.getMail())
                .telephone(userParam.getTelephone()).remark(userParam.getRemark()).username(userParam.getName()).status(userParam.getStatus())
                .build();
        sysUser.setOperator("System-Update");
        sysUser.setOperateIp("127.0.0.1");
        sysUser.setOperateTime(new Date());
        sysUserMapper.updateByPrimaryKeySelective(sysUser);
    }


    public ResultVO login(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        SysUser sysUser = findByKeyword(username);
        String errorMsg;
        if (StringUtils.isBlank(username)) {
            errorMsg = "用户名不能为空";
        } else if (StringUtils.isBlank(password)) {
            errorMsg = "密码不能为空";
        }else if (sysUser == null){
            errorMsg = "用户名不存在";
        } else if (!sysUser.getPassword().equals(MD5Util.encrypt(password))) {
            errorMsg = "密码错误";
        } else if (sysUser.getStatus() != 1) {
            errorMsg = "用户被冻结,请联系管理员";
        } else {
            request.getSession().setAttribute("user", sysUser);
            return ResultVOUtil.success();
        }
        return ResultVOUtil.error(errorMsg);
    }

    public PageResult<SysUser> getPageByDeptId(Integer deptId, PageQuery pageQuery) {
        ValidatorUtil.check(pageQuery);
        int count = sysUserMapper.countByDeptId(deptId);
        if (count > 0) {
            List<SysUser> list = sysUserMapper.getPageByDeptId(deptId, pageQuery);
            return PageResult.<SysUser>builder().total(count).data(list).build();
        }
        return PageResult.<SysUser>builder().build();
    }

    private SysUser findByKeyword(String keyword) {
        return sysUserMapper.findByKeyword(keyword);
    }

    private void check(UserParam userParam) {
        ValidatorUtil.check(userParam);
        if (checkEmail(userParam.getId(), userParam.getMail())) {
            throw new PermissionException("邮箱已经存在");
        }
        if (checkTelephone(userParam.getId(), userParam.getTelephone())) {
            throw new PermissionException("联系方式已经存在");
        }
    }


    private boolean checkEmail(Integer userId, String email) {
        return sysUserMapper.countByEmail(email, userId) > 0 ;
    }


    private boolean checkTelephone(Integer userId, String telephone) {
        return sysUserMapper.countByEmail(telephone, userId) > 0 ;
    }



}
