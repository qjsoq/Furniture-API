package com.april.furnitureapi.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static org.springframework.util.StringUtils.hasText;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private JwtTokenProvider tokenProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<String> token = retrieveTokenFromRequest(request);
        if(token.isEmpty()){
            filterChain.doFilter(request, response);
            return;
        }
        String presentToken = token.get();
        Optional<DecodedJWT> decodedJWT = tokenProvider.decodedJwt(presentToken);
        if(decodedJWT.isPresent()){
            String email = tokenProvider.getEmailFromToken(presentToken);
            var authentication = new UsernamePasswordAuthenticationToken(email, null,  Arrays.asList());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);

    }

    private Optional<String> retrieveTokenFromRequest(HttpServletRequest httpServletRequest){
        String header = httpServletRequest.getHeader("Authorization");
        if(hasText(header) && header.startsWith("Bearer ")){
            return Optional.of(header.substring(7));
        }
        return Optional.empty();
    }
}
