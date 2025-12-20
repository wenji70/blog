package com.wjc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;

@EnableWebSecurity  // 开启MVC security安全支持
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;
    @Value("${COOKIE.VALIDITY}")
    private Integer COOKIE_VALIDITY;


    /**
     * 重写configure(HttpSecurity http)方法，进行用户授权管理
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 1、自定义用户访问控制
        http.authorizeRequests()
                .antMatchers("/", "/page/**", "/article/**", "/login").permitAll()
                .antMatchers("/back/**", "/assets/**", "/user/**", "/article_img/**").permitAll()
                .antMatchers("/admin/**").hasRole("admin")
                .anyRequest().authenticated(); //除了上边设定的地址，其他所有请求都要通过认证，执行顺序从上到下

        // 2、自定义用户登录控制
        http.formLogin()
                .loginPage("/login")
                .usernameParameter("username").passwordParameter("password")
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                        String url = httpServletRequest.getParameter("url");
                        // 获取被拦截的原始访问路径
                        RequestCache requestCache = new HttpSessionRequestCache();
                        SavedRequest savedRequest = requestCache.getRequest(httpServletRequest, httpServletResponse);
                        if (savedRequest != null) {
                            // 如果存在原始拦截路径，登录成功后重定向到原始访问路径
                            httpServletResponse.sendRedirect(savedRequest.getRedirectUrl());
                        } else if (url != null && !url.equals("")) {
                            // 跳转到之前所在页面
                            URL fullURL = new URL(url);
                            httpServletResponse.sendRedirect(fullURL.getPath());
                        } else {
                            // 直接登录的用户，根据用户角色分别重定向到后台首页和前台首页
                            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                            boolean isAdmin = authorities.contains(new SimpleGrantedAuthority("ROLE_admin"));
                            if (isAdmin) {
                                httpServletResponse.sendRedirect("/admin");
                            } else {
                                httpServletResponse.sendRedirect("/");
                            }
                        }
                    }
                })
                // 用户登录失败处理
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                        // 登录失败后，取出原始页面url并追加在重定向路径上
                        String url = httpServletRequest.getParameter("url");
                        httpServletResponse.sendRedirect("/login?error&url=" + url);
                    }
                });

        // 3、设置用户登录后cookie有效期，默认值
        http.rememberMe().alwaysRemember(true).tokenValiditySeconds(COOKIE_VALIDITY);
        // 4、自定义用户退出控制
        http.logout().logoutUrl("/logout").logoutSuccessUrl("/");

        // 5、针对访问无权限页面出现的403页面进行定制处理
        http.exceptionHandling().accessDeniedHandler(new AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
                // 如果是权限访问异常，则进行拦截到指定错误页面
                RequestDispatcher dispatcher = httpServletRequest.getRequestDispatcher("/errorPage/comm/error_403");
                dispatcher.forward(httpServletRequest, httpServletResponse);
            }
        });
    }


    /**
     * 重写configure(AuthenticationManagerBuilder auth)方法，进行自定义用户认证
     * @param auth
     * @throws Exception
     */
    // 在使用JDBC身份认证时要注意以下几点
    // .创建用户表时， 用户名username必须唯一， 因为Security在进行用户查询时先判断username是否唯一
    // . 创建t用户表时， 必须额外定义一个tinyint类型的字段(对应boolean) ，用于校验用户是否合法(默认合法)
    // •初始化用户表数据时，插入的用户密码password必须是对应编码器编码后的密码
    //      初始化权限表t_ authority数据时， t authority值必须带有“ROLE_”前缀，而默认的用户角色值则是对应权限值去掉“ROLE_”前缀
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 密码需要设置编码器
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // 使用JDBC进行身份认证
        String userSQL ="select username,password,valid from t_user where username = ?";
        String authoritySQL ="select u.username,a.authority from t_user u,t_authority a, t_user_authority ua where ua.user_id=u.id and ua.authority_id=a.id and u.username =?";
        auth.jdbcAuthentication().passwordEncoder(encoder)//告诉Spring Security使用jdbc的方式来查询用户和权限,passwordEncoder()指定密码加密规则，用户的密码数据应该以同样的方式进行加密存储
                .dataSource(dataSource)//dataSource()方法指定数据库连接信息
                .usersByUsernameQuery(userSQL)
                .authoritiesByUsernameQuery(authoritySQL);//usersByUsernameQuery()和authoritiesByUsernameQuery()方法分别定义了查询用户和权限信息的sql语句
    }
    //定义用户查询SQL语句时，必须返回用户名username、密码password、是否有效用户valid3个字段。
    //定义权限查询SQL语句时，必须返回用户名username、权限authority两个字段。
}