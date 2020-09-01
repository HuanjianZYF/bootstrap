package cn.programer.zyf.auth.common;

import cn.programer.zyf.auth.domain.entity.RoleDO;
import cn.programer.zyf.auth.domain.entity.UserDO;
import cn.programer.zyf.auth.service.RoleService;
import cn.programer.zyf.auth.service.UserService;
import cn.programer.zyf.core.util.StringUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.session.Session;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zyf
 * @date 2020-09-01 10:49
 **/
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        Session session = SecurityUtils.getSubject().getSession();
        UserDO userDO = (UserDO) session.getAttribute("user");
        if (userDO == null) {
            throw new RuntimeException("请重新登录！");
        }

        // 获取角色
        List<RoleDO> roles = roleService.listUserRole(userDO.getId());
        if (!CollectionUtils.isEmpty(roles)) {
            authorizationInfo.setRoles(roles.stream()
                    .map(RoleDO::getCode)
            .collect(Collectors.toSet()));
        }

        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String userName = (String) authenticationToken.getPrincipal();
        String password = new String((char[]) authenticationToken.getCredentials());

        UserDO userDO = userService.getByUserName(userName);
        if (userDO == null) {
            throw new RuntimeException("用户不存在");
        }
        if (StringUtil.isEmpty(userDO.getPassword()) || !comparePassword(userDO.getPassword(), password, userDO.getSalt())) {
            throw new RuntimeException("密码错误");
        }

        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute("user", userDO);
        return new SimpleAuthenticationInfo(userName, password, this.getName());
    }

    /**
     * 比较密码的md5是否相等
     * @param secret 散列后的密码
     * @param password 密码明码
     * @param salt 盐
     * @return
     */
    private boolean comparePassword(String secret, String password, String salt) {
        String md5 = new Md5Hash(password, salt).toString();
        return secret.equals(md5);
    }

    @Override
    public String getName() {
        return "shiroName";
    }
}
