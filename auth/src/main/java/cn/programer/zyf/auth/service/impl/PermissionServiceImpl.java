package cn.programer.zyf.auth.service.impl;

import cn.programer.zyf.auth.dao.PermissionMapper;
import cn.programer.zyf.auth.domain.entity.PermissionDO;
import cn.programer.zyf.auth.domain.query.PermissionQuery;
import cn.programer.zyf.auth.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zyf
 * @date 2020-09-01 14:32
 **/
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public List<PermissionDO> listRolePermission(String roleCode) {
        PermissionQuery query = new PermissionQuery();
        query.setRoleCode(roleCode);
        return permissionMapper.listRolePermission(query);
    }
}
