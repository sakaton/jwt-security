## 加密技术

### 加密方式分类

#### 单向加密

* `BASE64 `严格地说，属于编码格式，而非加密算法
* `MD5`(Message Digest algorithm 5，信息摘要算法)
* `SHA`(Secure Hash Algorithm，安全散列算法)
* `HMAC`(Hash Message Authentication Code，散列消息鉴别码)
* `PBKDF2`
* `scrypt`
* `bcrypt`

#### 双向加密

- `DES` (Data Encryption Standard，数据加密算法)
- `PBE`  (Password-based encryption，基于密码验证)
- `RSA` (算法的名字以发明者的名字命名：Ron Rivest, AdiShamir 和Leonard Adleman)
- `DH` (Diffie-Hellman算法，密钥一致协议)
- `DSA` (Digital Signature Algorithm，数字签名)
- `ECC` (Elliptic Curves Cryptography，椭圆曲线密码编码学)







## Security · Servlet

### WebApplicationInitializer

* javax.servlet.ServletContainerInitializer

  * org.springframework.web.SpringServletContainerInitializer

    * org.springframework.web.WebApplicationInitializer

      * org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer

      * 进入 `onStartup` 方法

      * 初始化  `org.springframework.web.filter.DelegatingFilterProxy`

        `org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer#insertSpringSecurityFilterChain`

### FilterChainProxy

* 生产 生成被代理 filter
  * org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration
    * `org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration#springSecurityFilterChain`
  
* 程序经过调用之后会进入 `DelegatingFilterProxy`
  * 然后委派给 `FilterChainProxy`  然后进入 `org.springframework.security.web.FilterChainProxy.VirtualFilterChain`
  * 迭代交给其他`filter` 处理
  
* `Firewalled`
  
  * 在`org.springframework.security.web.FilterChainProxy#doFilterInternal` 处理中转为`FirewalledRequest`
  
* `org.springframework.security.web.SecurityFilterChain` 接口

  * 默认实现 `DefaultSecurityFilterChain` 

  * 在 `FilterChainProxy` 代理之后 遍历寻找进入哪个 `Chain` 然后获取到 `Filter` 列表

    ```java
    org.springframework.security.web.FilterChainProxy#doFilterInternal;
    	private void doFilterInternal(ServletRequest request, ServletResponse response,
    			FilterChain chain) throws IOException, ServletException {
    
    		FirewalledRequest fwRequest = firewall
    				.getFirewalledRequest((HttpServletRequest) request);
    		HttpServletResponse fwResponse = firewall
    				.getFirewalledResponse((HttpServletResponse) response);
    
    		List<Filter> filters = getFilters(fwRequest);
    
    		if (filters == null || filters.size() == 0) {
    			if (logger.isDebugEnabled()) {
    				logger.debug(UrlUtils.buildRequestUrl(fwRequest)
    						+ (filters == null ? " has no matching filters"
    								: " has an empty filter list"));
    			}
    
    			fwRequest.reset();
    
    			chain.doFilter(fwRequest, fwResponse);
    
    			return;
    		}
    
    		VirtualFilterChain vfc = new VirtualFilterChain(fwRequest, chain, filters);
    		vfc.doFilter(fwRequest, fwResponse);
    	}
    	private List<Filter> getFilters(HttpServletRequest request) {
    		for (SecurityFilterChain chain : filterChains) {
    			if (chain.matches(request)) {
    				return chain.getFilters();
    			}
    		}
    
    		return null;
    	}
    ```



### HttpSecurity

* `HttpSecurity` 核心是`Filter`

* 每一个Filter 对应一个功能

  1. `org.springframework.security.web.csrf.CsrfFilter`

  2. `org.springframework.security.web.header.HeaderWriterFilter`

  3. `org.springframework.security.web.session.SessionManagementFilter`

  4. `org.springframework.web.filter.CorsFilter`

  5. `org.springframework.security.web.access.ExceptionTranslationFilter`
     * 该接口过滤器用于处理处理异常情况捕捉 401 和 403

  6. `org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter`

     * 需要认证的过滤器实现继承 `AbstractAuthenticationProcessingFilter`通过该接口方法实现

     * `AuthenticationFailureHandler`

     * `AuthenticationSuccessHandler`
     * `SessionAuthenticationStrategy ` 认证成功回调

  7. `org.springframework.security.web.access.intercept.FilterSecurityInterceptor`
     * 实现权限控制核心过滤器 `org.springframework.security.access.intercept.AbstractSecurityInterceptor`

  8. `org.springframework.security.web.authentication.AnonymousAuthenticationFilter`
     * 用于处理`SecurityContextHolder#getContext()` 为空是设置为匿名`AnonymousAuthenticationToken`

  9. `org.springframework.security.web.authentication.www.BasicAuthenticationFilter`

  10. `org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter`
  11. `org.springframework.security.web.context.SecurityContextPersistenceFilter`
      * 用于处理 通过 cookie 里面获取到ID 然后转化为获取到`SecurityContext` `Authentication`

* Filter 扩展配置

  1. `org.springframework.security.config.annotation.web.configurers.CsrfConfigurer`
     * 存储
       * `org.springframework.security.web.csrf.CsrfTokenRepository`
         * `CookieCsrfTokenRepository`
         * `HttpSessionCsrfTokenRepository`
         * `LazyCsrfTokenRepository`

  2. `org.springframework.security.config.annotation.web.configurers.HeadersConfigurer`

  3. `org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer`
     * 存储
       * `ServerSecurityContextRepository`
         * `HttpSessionSecurityContextRepository`
         * `NullSecurityContextRepository`
       * `org.springframework.security.core.session.SessionRegistry`
         * `org.springframework.security.core.session.SessionRegistryImpl`

  4. `org.springframework.security.config.annotation.web.configurers.CorsConfigurer`

  5. `org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer`
     * `HttpSecurity#exceptionHandling()`  该返回返回`Configurer`
     * `ExceptionHandlingConfigurer#accessDeniedHandler` 用于处理 没有权限的情况
     * `ExceptionHandlingConfigurer#authenticationEntryPoint` 用于处理认证失败的情况

  6. `org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer`

  7. `org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer`
     * `HttpSecurity#authorizeRequests()`

  8. `org.springframework.security.config.annotation.web.configurers.AnonymousConfigurer`

  9. `org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer`

  10. `org.springframework.security.config.annotation.web.configurers.RememberMeConfigurer`

      * 存储

        * `org.springframework.security.web.authentication.RememberMeServices`

          * 默认实现`PersistentTokenBasedRememberMeServices`

          * `PersistentTokenRepository`

  11. `org.springframework.security.config.annotation.web.configurers.SecurityContextConfigurer`

* Filter 内置排序

  * `org.springframework.security.config.annotation.web.builders.FilterComparator` 通过该类实现内部排序

* 禁止掉Filter

  * `AbstractHttpConfigurer#disable`

* 扩展

  * 通过`org.springframework.security.config.annotation.AbstractConfiguredSecurityBuilder#apply(C)` 实现可配置的Buildr式Filter
  * 也可以通过`org.springframework.security.config.annotation.web.builders.HttpSecurity#addFilter` 添加策略Filter

* 

### WebSecurity

* `WebSecurity` 核心是`Chain`
* `WebSecurity#ignoring` 处理忽略不仅过核心`Chain`
  * `WebSecurity#addSecurityFilterChainBuilder` 自定义 Chain



### Web 抽象Filter

* `org.springframework.web.filter.OncePerRequestFilter`
* `org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter`
* `org.springframework.web.filter.GenericFilterBean`



### Security Core

* `AuthenticationManager`
  
  * 默认实现`org.springframework.security.authentication.ProviderManager`
    * 处理多个`AuthenticationProvider`
  
* `AuthenticationProvider`

* `Authentication`
  
  凭证
  
  * `Authentication#getAuthorities` 权限
    * 辅助工具类`AuthorityUtils`
  * `Authentication#getCredentials` 凭证 通常会设置为 密码
  * `Authentication#getDetails` 认证详细系信息 `UserDetails`
  * `Authentication#getPrincipal` 通常为用户名
  
* `UserDetailsService`
  
  * `UserDetails`
    * `UserDetails#getAuthorities`
      * 用于存储用户角色或者其他标识用户权限信息
  
* `SecurityContextHolder`

  * 持有当前请求的`SecurityContext`

* `AbstractSecurityInterceptor`
  
  * `MethodSecurityInterceptor` 
  * `FilterSecurityInterceptor`  Web Servlet 的实现拦截器权限
  
* `AccessDecisionManager`

* `SecurityMetadataSource`

  * 实现接口
    * `FilterInvocationSecurityMetadataSource`  Web Servlet
    * `MethodSecurityMetadataSource` 编程方式

  * `SecurityConfig#createList` 辅助工具类

* `PasswordEncoder`

  * `BCryptPasswordEncoder` 推荐使用

* `SecurityContext`

* `AbstractUserDetailsAuthenticationProvider`

  * 用于`UserDetails` 认证

