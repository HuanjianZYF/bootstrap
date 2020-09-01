package cn.programer.zyf.auth.common;

import cn.programer.zyf.auth.domain.entity.PermissionDO;
import cn.programer.zyf.auth.service.PermissionService;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zyf
 * @date 2020-09-01 13:54
 **/
public class ShiroRolePermissionResolver implements RolePermissionResolver {

    @Autowired
    private PermissionService permissionService;

    @Override
    public Collection<Permission> resolvePermissionsInRole(String s) {
        Collection<Permission> resultList = new ArrayList<>();

        List<PermissionDO> list = permissionService.listRolePermission(s);
        if (CollectionUtils.isEmpty(list)) {
            return resultList;
        }

        resultList = list.stream().map(PermissionDO::getCode)
                .map(ShiroPermission::new)
                .collect(Collectors.toList());

        return resultList;
    }
}
