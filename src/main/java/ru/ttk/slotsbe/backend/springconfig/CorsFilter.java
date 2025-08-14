package ru.ttk.slotsbe.backend.springconfig;

import org.springframework.web.filter.OncePerRequestFilter;
import ru.ttk.slotsbe.backend.model.ClientUser;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ttk.slotsbe.backend.service.SearchService;
import ru.ttk.slotsbe.backend.service.UserService;

public class CorsFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(CorsFilter.class);
    private final SearchService searchService;

    private UserService userService;

    public CorsFilter(
            UserService userService,
            SearchService searchService
    ) {
        this.userService = userService;
        this.searchService = searchService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
//        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
//        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, PATCH");
//        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Headers, Access-Control-Allow-Origin, Authorization, Content-Type, XSRF-Token");
//        response.addHeader("Access-Control-Expose-Headers", "xsrf-token");

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.addHeader("Access-Control-Expose-Headers", "*");

        response.addHeader("Access-Control-Allow-Credentials", "true");

        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else if (checkAccess(request)) {
            filterChain.doFilter(request, response);
            if (userService.getExpired()) {
                String errMsh = "Password expired";
                log.info(errMsh);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, errMsh);
                return;
            }
//                log.info("Password not expired");

        } else {
//                filterChain.doFilter(request, response);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
        }
    }

    private boolean checkAccess(HttpServletRequest request) {
        String service = request.getRequestURI();

        if (
                service.startsWith("/api/login") ||
                        service.startsWith("/api/logout") ||
                        service.startsWith("/api/session") ||

                        //service.startsWith("/swagger-ui.html") ||
                        //service.startsWith("/swagger-resources")
                        //service.startsWith("/webjars") ||
                        service.contains("swagger-ui") ||
                        service.contains("swagger-resources") ||
                        service.contains("swagger-config") ||
                        service.contains("webjars") ||
                        service.contains("v3/api-docs") ||
                        service.startsWith("/ws")
        ) {
    //            log.info("CheckAccess: successfully");
            return true;
        }

        ClientUser user = userService.getCurrentUser();

        return true;
    }
}

