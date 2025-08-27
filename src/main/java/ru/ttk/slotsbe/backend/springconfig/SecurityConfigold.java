//package ru.intelsource.s3traf.backend.springconfig;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.access.channel.ChannelProcessingFilter;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//import org.springframework.web.bind.annotation.RequestMethod;
//import ru.intelsource.s3traf.backend.security.*;
//import ru.intelsource.s3traf.backend.service.LicService;
//import ru.intelsource.s3traf.backend.service.SearchService;
//import ru.intelsource.s3traf.backend.service.UserService;
//import org.springframework.security.core.userdetails.UserDetailsService;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
//public class SecurityConfigold {
//
//    private static final Logger log = LoggerFactory.getLogger(CorsFilter.class);
//
//    private static final String PASSWORD_PARAMETER = "password";
//    private static final String USERNAME_PARAMETER = "username";
//    private static final String LOGIN_URL = "/api/login";
//
//    private final UserService userService;
//    private final LicService licService;
//    private final SearchService searchService;
//    private final DaoUserDetailsService daoUserDetailsService;
//
//    public SecurityConfigold(UserService userService, LicService licService, SearchService searchService, DaoUserDetailsService daoUserDetailsService) {
//        this.userService = userService;
//        this.licService = licService;
//        this.searchService = searchService;
//        this.daoUserDetailsService = daoUserDetailsService;
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
//        AuthenticationFilter filter = new AuthenticationFilter();
//        filter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(LOGIN_URL, RequestMethod.POST.name()));
//        filter.setPasswordParameter(PASSWORD_PARAMETER);
//        filter.setUsernameParameter(USERNAME_PARAMETER);
//        filter.setAuthenticationManager(authenticationManager);
//        filter.setAuthenticationSuccessHandler(new SecurityAuthenticationSuccessHandler());
//        filter.setAuthenticationFailureHandler(new SecurityAuthenticationFailureHandler());
//
//        http
//                .addFilterBefore(new CorsFilter(userService, licService, searchService), ChannelProcessingFilter.class)
//                .addFilter(filter)
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/**").permitAll()
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/api/logout")
//                        .deleteCookies("JSESSIONID")
//                        .logoutSuccessHandler(new SecurityLogoutSuccessHandler())
//                        .invalidateHttpSession(true)
//                )
//                .headers(headers -> headers.frameOptions().disable());
//
//        return http.build();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return daoUserDetailsService;
//    }
//}
