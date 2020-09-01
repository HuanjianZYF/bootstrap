package cn.programer.zyf.auth.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.shiro.authz.Permission;

/**
 * @author zyf
 * @date 2020-09-01 13:48
 **/
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShiroPermission implements Permission {

    private String permissionCode;

    @Override
    public boolean implies(Permission permission) {
        if (!(permission instanceof ShiroPermission)) {
            return false;
        }
        return ((ShiroPermission) permission).getPermissionCode().equals(permissionCode);
    }
}
