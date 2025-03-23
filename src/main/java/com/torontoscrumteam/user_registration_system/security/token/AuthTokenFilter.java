package com.torontoscrumteam.user_registration_system.security.token;

import com.torontoscrumteam.user_registration_system.entity.User;
import com.torontoscrumteam.user_registration_system.repository.UserRepository; // 需要注入 UserRepository
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository; // 注入 UserRepository

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String path = request.getServletPath();
            if (path.startsWith("/api/auth/signup") || path.startsWith("/api/auth/login")) {
                filterChain.doFilter(request, response);
                return;
            }

            Optional<String> jwt = parseJwt(request);
            if (jwt.isPresent()) {
                String token = jwt.get();
                String userIdStr = redisTemplate.opsForValue().get("user:token:" + token);
                if (userIdStr != null) {
                    try {
                        Long userId = Long.parseLong(userIdStr);
                        Optional<User> userOptional = userRepository.findById(userId);
                        if (userOptional.isPresent()) {
                            User user = userOptional.get();
                            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());
                            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                    } catch (NumberFormatException e) {
                        logger.error("Invalid user ID format in Redis: {}", userIdStr);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
    }

    private Optional<String> parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return Optional.of(headerAuth.substring(7));
        }
        return Optional.empty();
    }
}