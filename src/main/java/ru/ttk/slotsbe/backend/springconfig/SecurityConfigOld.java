//package ru.ttk.slotsbe.backend.springconfig;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.web.access.channel.ChannelProcessingFilter;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//import org.springframework.web.bind.annotation.RequestMethod;
//import ru.ttk.slotsbe.backend.security.*;
//import ru.ttk.slotsbe.backend.service.SearchService;
//import ru.ttk.slotsbe.backend.service.UserService;
//
////import jakarta.inject.Inject;
//
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    private static final Logger log = LoggerFactory.getLogger(CorsFilter.class);
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private SearchService searchService;
//
//    private static final String PASSWORD_PARAMETER = "password";
//    private static final String USERNAME_PARAMETER = "username";
//
//    private static final String LOGIN_URL = "/api/login";
//
//    @Autowired
//    private DaoUserDetailsService daoUserDetailsService;
//
//    @Bean
//    public AuthenticationFilter authenticationFilter() throws Exception {
//        AuthenticationFilter filter = new AuthenticationFilter();
//
//        filter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(LOGIN_URL, RequestMethod.POST.name()));
//        filter.setPasswordParameter(PASSWORD_PARAMETER);
//        filter.setUsernameParameter(USERNAME_PARAMETER);
//        filter.setAuthenticationManager(authenticationManagerBean());
//        filter.setAuthenticationSuccessHandler(new SecurityAuthenticationSuccessHandler());
//        filter.setAuthenticationFailureHandler(new SecurityAuthenticationFailureHandler());
//
//        return filter;
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .addFilterBefore(new CorsFilter(userService, searchService), ChannelProcessingFilter.class)
//                .addFilter(authenticationFilter())
//                .csrf().disable()
//                .authorizeRequests()
//                    .antMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html","/**").permitAll()
//                     //env.getRequiredProperty("common.control.allow.origin")
//                .and()
//                .logout().permitAll().logoutUrl("/api/logout").deleteCookies("JSESSIONID")
//                   .logoutSuccessHandler(new SecurityLogoutSuccessHandler())
//                   .invalidateHttpSession(true)
//                .and()
//                .headers().frameOptions().disable();
////        log.info("HttpSecurity configured");
//
//    }
//
//    @Autowired
//    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(daoUserDetailsService);
//    }
//}
