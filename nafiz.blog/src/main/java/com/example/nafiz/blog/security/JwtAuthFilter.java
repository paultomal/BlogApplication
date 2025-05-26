package com.example.nafiz.blog.security;

import com.example.nafiz.blog.component.UserInfoUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserInfoUserDetailsService userDetailsService;
    private String username = null;
    private String email = null;
    private List<GrantedAuthority> roles = null; // Change to List<GrantedAuthority>



    public JwtAuthFilter(JwtService jwtService,
                         UserInfoUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestUri = request.getRequestURI();

        if (isUrlExcludedFromAuthentication(requestUri)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        String token = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtService.extractUsername(token);
        } else {
            Cookie[] cookies = request.getCookies();

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("AuthToken".equals(cookie.getName())) {
                        token = cookie.getValue();
                        username = jwtService.extractUsername(token);
                        break;
                    }
                }
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (token != null) {
                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    this.roles = (List<GrantedAuthority>) userDetails.getAuthorities();

                }
            }
        }

        filterChain.doFilter(request, response);
    }


    private boolean isUrlExcludedFromAuthentication(String requestUri) {
        String[] excludedUrls = {"/authenticate"};

        for (String excludedUrl : excludedUrls) {
            if (requestUri.equals(excludedUrl) || requestUri.startsWith(excludedUrl + "/")) {
                return true;
            }
        }
        return false;
    }

    public String getCurrentUser() {
        return email;
    }

    public Boolean isSupervisor() {
        return roles != null && roles.stream().anyMatch(role -> role.getAuthority().equals("ROLE_SUPERVISOR"));
    }

    public Boolean isEmployee() {
        return roles != null && roles.stream().anyMatch(role -> role.getAuthority().equals("ROLE_EMPLOYEE"));
    }
}
