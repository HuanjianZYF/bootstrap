package cn.programer.zyf.auth.service.impl;

import cn.programer.zyf.auth.dao.RoleMapper;
import cn.programer.zyf.auth.domain.entity.RoleDO;
import cn.programer.zyf.auth.domain.query.RoleQuery;
import cn.programer.zyf.auth.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zyf
 * @date 2020-09-01 14:16
 **/
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<RoleDO> listUserRole(Long userId) {
        RoleQuery query = new RoleQuery();
        query.setUserId(userId);
        return roleMapper.listUserRoles(query);
    }
}
