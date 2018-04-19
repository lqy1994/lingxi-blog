package cn.edu.sdu.wh.lqy.lingxi.blog.config.shiro;//package cn.edu.sdu.wh.lqy.lingxi.doc.config.shiro;
//
//import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
//import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
//import org.apache.shiro.cache.ehcache.EhCacheManager;
//import org.apache.shiro.codec.Base64;
//import org.apache.shiro.crypto.hash.Md5Hash;
//import org.apache.shiro.mgt.SecurityManager;
//import org.apache.shiro.session.SessionListener;
//import org.apache.shiro.session.mgt.SessionManager;
//import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
//import org.apache.shiro.session.mgt.eis.SessionDAO;
//import org.apache.shiro.spring.LifecycleBeanPostProcessor;
//import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
//import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
//import org.apache.shiro.web.mgt.CookieRememberMeManager;
//import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
//import org.apache.shiro.web.servlet.SimpleCookie;
//import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
//import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.core.io.ClassPathResource;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.LinkedHashMap;
//
//@Configuration
//public class ShiroConfiguration {
//
//    @Value("${shiro.cipherkey}")
//    private String cipherKey = "U3ByaW5nQmxhZGUAAAAAAA==";
//
//    @Bean
////    @ConditionalOnMissingBean
//    public EhCacheManager ehCacheManager() {
//        EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();
//        cacheManagerFactoryBean.setConfigLocation(new ClassPathResource("classpath:config/Ehcache.xml"));
//
//        EhCacheManager ehCacheManager = new EhCacheManager();
//        ehCacheManager.setCacheManager(cacheManagerFactoryBean.getObject());
//        return ehCacheManager;
//    }
//
//    @Bean
////    @ConditionalOnMissingBean
////    @ConditionalOnClass(SecurityManager.class)
//    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
//        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
//        shiroFilterFactoryBean.setSecurityManager(securityManager);
//        shiroFilterFactoryBean.setLoginUrl("/login");
//        shiroFilterFactoryBean.setSuccessUrl("/index");
//        shiroFilterFactoryBean.setUnauthorizedUrl("/login");
//
//        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
//        filterChainDefinitionMap.put("/static/css/**", "anon");
//        filterChainDefinitionMap.put("/static/js/**", "anon");
//        filterChainDefinitionMap.put("/static/fonts/**", "anon");
//        filterChainDefinitionMap.put("/static/img/**", "anon");
//        filterChainDefinitionMap.put("/static/layui/**", "anon");
//
//        filterChainDefinitionMap.put("/404", "anon");
//        filterChainDefinitionMap.put("/error", "anon");
//
//        filterChainDefinitionMap.put("/druid/**", "anon");
//        filterChainDefinitionMap.put("/register", "anon");
//        filterChainDefinitionMap.put("/login", "anon");
//        filterChainDefinitionMap.put("/gifCode", "anon");
//        filterChainDefinitionMap.put("/logout", "logout");
//        filterChainDefinitionMap.put("/", "anon");
//        filterChainDefinitionMap.put("/**", "user");
//
//        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
//
//        return shiroFilterFactoryBean;
//    }
//
//    @Bean
////    @Order(Ordered.HIGHEST_PRECEDENCE + 2000000)
////    @ConditionalOnMissingBean
////    @ConditionalOnBean({ShiroRealm.class, RememberMeManager.class, CacheManager.class, SessionManager.class})
//    public SecurityManager securityManager(SessionManager sessionManager){
//        DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();
//        securityManager.setRealm(shiroRealm(retryLimitHashedCredentialsMatcher()));
//        securityManager.setRememberMeManager(rememberMeManager());
//        securityManager.setCacheManager(ehCacheManager());
//        securityManager.setSessionManager(sessionManager);
//
//        return securityManager;
//    }
//
//    @Bean
//    public RetryLimitHashedCredentialsMatcher retryLimitHashedCredentialsMatcher() {
//        RetryLimitHashedCredentialsMatcher matcher = new RetryLimitHashedCredentialsMatcher(ehCacheManager());
//        matcher.setHashAlgorithmName(Md5Hash.ALGORITHM_NAME);
//        matcher.setStoredCredentialsHexEncoded(true);
//        matcher.setHashIterations(6);
//        return matcher;
//    }
//
//    @Bean(name = "lifecycleBeanPostProcessor")
////    @ConditionalOnMissingBean
//    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
//        return new LifecycleBeanPostProcessor();
//    }
//
//    @Bean
//    public AuthorizationAttributeSourceAdvisor attributeSourceAdvisor(SecurityManager securityManager) {
//        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
//        advisor.setSecurityManager(securityManager);
//        return advisor;
//    }
//
//    @Bean
////    @ConditionalOnMissingBean
//    public ShiroRealm shiroRealm(HashedCredentialsMatcher credentialsMatcher){
//        ShiroRealm shiroRealm = new ShiroRealm();
//        shiroRealm.setCachingEnabled(true);
//        shiroRealm.setCredentialsMatcher(credentialsMatcher);
//        return shiroRealm;
//    }
//
//    @Bean
////    @ConditionalOnMissingBean
//    public SimpleCookie rememberMeCookie() {
//        SimpleCookie cookie = new SimpleCookie("rememberMe");
//        cookie.setMaxAge(7 * 24 * 60 * 60);
//        cookie.setHttpOnly(true);
//        return cookie;
//    }
//
//    @Bean
////    @ConditionalOnMissingBean
////    @ConditionalOnClass(Base64.class)
////    @ConditionalOnProperty(prefix = "shiro", name = "cipher-key", matchIfMissing = false)
//    public CookieRememberMeManager rememberMeManager() {
//        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
//        cookieRememberMeManager.setCookie(rememberMeCookie());
//        cookieRememberMeManager.setCipherKey(Base64.decode("U3ByaW5nQmxhZGUAAAAAAA=="));
//        return cookieRememberMeManager;
//    }
//
//    @Bean
////    @ConditionalOnMissingBean
//    @DependsOn({"lifecycleBeanPostProcessor"})
//    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
//        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
//        advisorAutoProxyCreator.setProxyTargetClass(true);
//        return advisorAutoProxyCreator;
//    }
//
//    @Bean
////    @ConditionalOnMissingBean
////    @ConditionalOnClass(SecurityManager.class)
//    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
//        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
//        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
//        return authorizationAttributeSourceAdvisor;
//    }
//
//    @Bean
////    @ConditionalOnMissingBean
//    public ShiroDialect shiroDialect() {
//        return new ShiroDialect();
//    }
//
//    @Bean
////    @ConditionalOnMissingBean
//    public SessionDAO sessionDAO() {
//        MemorySessionDAO sessionDAO = new MemorySessionDAO();
//        return sessionDAO;
//    }
//
//    @Bean
////    @Order(Ordered.HIGHEST_PRECEDENCE + 1)
////    @ConditionalOnMissingBean
////    @ConditionalOnClass({SessionListener.class, SessionDAO.class})
//    public SessionManager sessionManager() {
//        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
//        Collection<SessionListener> listeners = new ArrayList<>();
//        listeners.add(new ShiroSessionListener());
//        sessionManager.setSessionListeners(listeners);
//        sessionManager.setSessionDAO(sessionDAO());
//        return sessionManager;
//    }
//
//}
