package cn.programer.zyf.auth.common;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.PermissionResolver;

/**
 * @author zyf
 * @date 2020-09-01 13:52
 **/
public class ShiroPermissionResolver implements PermissionResolver {

    @Override
    public Permission resolvePermission(String s) {
        return new ShiroPermission(s);
    }
}
