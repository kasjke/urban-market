package com.example.urbanmarket.security.jwt;

import com.example.urbanmarket.entity.user.UserRepository;
import com.example.urbanmarket.entity.user.UserEntity;
import com.example.urbanmarket.exception.exceptions.general.CustomNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private static final String UNAUTHORIZED_MESSAGE = "Unauthorized";


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String jwt = jwtService.extractTokenFromRequest(request);
        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!jwtService.isTokenValid(jwt)) {
            log.warn("Invalid JWT token for URI: {}", request.getRequestURI());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, UNAUTHORIZED_MESSAGE);
            return;
        }

        String userId = jwtService.extractUserId(jwt);
        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            log.info("Authenticating user with ID: {}", userId);
            UserEntity user =  userRepository.findById(userId)
                    .orElseThrow(() -> new CustomNotFoundException("User",userId));

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    Collections.singletonList(user.getRole())
            );

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
            log.info("Successfully authenticated user with ID: {}", userId);
        }
        filterChain.doFilter(request, response);
    }
}
