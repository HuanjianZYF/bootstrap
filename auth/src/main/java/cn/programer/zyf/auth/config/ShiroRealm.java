package cn.programer.zyf.auth.config;

import cn.programer.zyf.auth.domain.entity.UserDO;
import cn.programer.zyf.auth.service.UserService;
import cn.programer.zyf.core.util.StringUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zyf
 * @date 2020-09-01 10:49
 **/
@Component
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String userName = (String) authenticationToken.getPrincipal();
        String password = new String((char[]) authenticationToken.getCredentials());

        UserDO userDO = userService.getByUserName(userName);
        if (userDO == null) {
            throw new RuntimeException("用户不存在");
        }
        if (StringUtil.isEmpty(userDO.getPassword()) || !userDO.getPassword().equals(password)) {
            throw new RuntimeException("密码错误");
        }

        return new SimpleAuthenticationInfo(userName, userDO.getPassword(), this.getName());
    }

    @Override
    public String getName() {
        return "shiroName";
    }
}
