package com.huii.admin.security.config;

import com.huii.admin.security.filter.JwtAuthenticationFilter;
import com.huii.admin.security.filter.LoginKaptchaFilter;
import com.huii.admin.security.handler.*;
import com.huii.admin.security.service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Order(1)
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    LoginFailureHandler loginFailureHandler;

    @Autowired
    LoginSuccessHandler loginSuccessHandler;

    @Autowired
    LogoutSuccessHandler logoutSuccessHandler;

    @Autowired
    AuthenticationAccessDeniedHandler authenticationAccessDeniedHandler;

    @Autowired
    AuthenticationEntryPointHandler authenticationEntryPointHandler;

    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    LoginKaptchaFilter kaptchaFilter;

    @Bean
    @Lazy
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService UserDetailServiceImpl() {
        return new UserDetailServiceImpl();
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * ???????????????????????????/??????
     */
    private static final String[] URL_WHITELIST = {
            //swagger??????
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/profile/**",
            "/v3/**",
            //????????????
            "/hello/**",
            "/auth/**",
            "/login",
            "/sys/user/pass/reset",
            //????????????
            "favicon.ico"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //??????csrf
                .cors().and().csrf().disable()
                // ??????session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                //????????????
                .and()
                .formLogin()
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler)
                //????????????
                .and()
                .logout()
                .logoutSuccessHandler(logoutSuccessHandler)
                //????????????
                .and()
                .authorizeRequests()
                .antMatchers(URL_WHITELIST).permitAll()
                .anyRequest().authenticated()

                //????????????
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPointHandler)
                .accessDeniedHandler(authenticationAccessDeniedHandler)
                //?????????
                .and()
                .addFilterBefore(jwtAuthenticationFilter, LogoutFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(kaptchaFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(UserDetailServiceImpl()).passwordEncoder(passwordEncoder());
    }
}
