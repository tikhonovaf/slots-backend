package ru.ttk.slotsbe.backend.springconfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.ttk.slotsbe.backend.security.*;
import ru.ttk.slotsbe.backend.service.SearchService;
import ru.ttk.slotsbe.backend.service.UserService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)  // Замена @EnableGlobalMethodSecurity
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(CorsFilter.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SearchService searchService;

    private static final String PASSWORD_PARAMETER = "password";
    private static final String USERNAME_PARAMETER = "username";
    private static final String LOGIN_URL = "/api/login";

    @Autowired
    private DaoUserDetailsService daoUserDetailsService;

    @Bean
    public AuthenticationFilter authenticationFilter(AuthenticationManager authenticationManager) {
        AuthenticationFilter filter = new AuthenticationFilter();
        filter.setRequiresAuthenticationRequestMatcher(
                new AntPathRequestMatcher(LOGIN_URL, RequestMethod.POST.name())
        );
        filter.setPasswordParameter(PASSWORD_PARAMETER);
        filter.setUsernameParameter(USERNAME_PARAMETER);
        filter.setAuthenticationManager(authenticationManager);
        filter.setAuthenticationSuccessHandler(new SecurityAuthenticationSuccessHandler());
        filter.setAuthenticationFailureHandler(new SecurityAuthenticationFailureHandler());
        return filter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(
                        new CorsFilter(userService, searchService),
                        ChannelProcessingFilter.class
                )
                .addFilter(authenticationFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class))))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/**"
                        ).permitAll()
                )
                .logout(logout -> logout
                        .permitAll()
                        .logoutUrl("/api/logout")
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessHandler(new SecurityLogoutSuccessHandler())
                        .invalidateHttpSession(true)
                )
                .headers(headers -> headers
                        .frameOptions(frame -> frame.disable())
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig
    ) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}