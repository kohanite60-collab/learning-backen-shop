package org.example.asimpleshop.config;


import jakarta.annotation.Resource;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.example.asimpleshop.realm.MyRealm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiroConfig {

    @Resource
    private MyRealm myRealm;

    // 配置securitymanager
    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(){
        // 1、创建defaultWebSecurityManager对象
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        // 2、创建加密对象，设置相关属性
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        hashedCredentialsMatcher.setHashIterations(1);
        // 3、将加密对象存储到myRealm中
        myRealm.setCredentialsMatcher(hashedCredentialsMatcher);
        // 4、将myRealm存入到defaultWebSecurityManager对象
        defaultWebSecurityManager.setRealm(myRealm);
        // 5、返回
        return defaultWebSecurityManager;
    }

    // 配置shiro内置过滤器拦截范围
    @Bean
    public DefaultShiroFilterChainDefinition shiroFilterChainDefinition(){
        DefaultShiroFilterChainDefinition defaultShiroFilterChainDefinition = new DefaultShiroFilterChainDefinition();
        // 设置不认证可以访问的资源
        defaultShiroFilterChainDefinition.addPathDefinition("/user/userLogin","anon");
        defaultShiroFilterChainDefinition.addPathDefinition("/login","anon");
        defaultShiroFilterChainDefinition.addPathDefinition("/login.jsp","anon");
        // 设置登出过滤器
        defaultShiroFilterChainDefinition.addPathDefinition("/user/logout","logout");
        // 设置需要进行登录认证的拦截范围
        defaultShiroFilterChainDefinition.addPathDefinition("/**","authc");
        return defaultShiroFilterChainDefinition;
    }





}
