package cn.programer.zyf.auth.config;

import cn.programer.zyf.auth.common.ShiroPermissionResolver;
import cn.programer.zyf.auth.common.ShiroRealm;
import cn.programer.zyf.auth.common.ShiroRolePermissionResolver;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zyf
 * @date 2020-09-01 10:44
 **/
@Configuration
public class ShiroConfig {

    @Bean
    public ShiroFilterFactoryBean shiroFilter(org.apache.shiro.mgt.SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        Map<String, String> filterChainDefinitionMap = new HashMap<String, String>();
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthc");
        shiroFilterFactoryBean.setSuccessUrl("/home/index");

        filterChainDefinitionMap.put("/*", "anon");
        filterChainDefinitionMap.put("/authc/index", "authc");
        filterChainDefinitionMap.put("/authc/admin", "roles[admin]");
        filterChainDefinitionMap.put("/authc/renewable", "perms[Create,Update]");
        filterChainDefinitionMap.put("/authc/removable", "perms[Delete]");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean(name = "shiroRealm")
    public ShiroRealm shiroRealm() {
        ShiroRealm shiroRealm = new ShiroRealm();
        shiroRealm.setCredentialsMatcher(simpleCredentialsMatcher());

        // 权限
        shiroRealm.setRolePermissionResolver(rolePermissionResolver());
        shiroRealm.setPermissionResolver(permissionResolver());

        return shiroRealm;
    }

    @Bean(name="simpleCredentialsMatcher")
    public SimpleCredentialsMatcher simpleCredentialsMatcher(){
        SimpleCredentialsMatcher simpleCredentialsMatcher = new SimpleCredentialsMatcher();
        return simpleCredentialsMatcher;
    }

    @Bean(name = "securityManager")
    public org.apache.shiro.mgt.SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm());
        return securityManager;
    }

    @Bean(name = "permissionResolver")
    public ShiroPermissionResolver permissionResolver() {
        return new ShiroPermissionResolver();
    }

    @Bean(name = "rolePermissionResolver")
    public ShiroRolePermissionResolver rolePermissionResolver() {
        return new ShiroRolePermissionResolver();
    }
}
