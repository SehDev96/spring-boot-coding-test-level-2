package com.accenture.codingtest.springbootcodingtest.filters;

import com.accenture.codingtest.springbootcodingtest.model.ApiErrorResponse;
import com.accenture.codingtest.springbootcodingtest.model.ApiResponse;
import com.accenture.codingtest.springbootcodingtest.model.LoginRequest;
import com.accenture.codingtest.springbootcodingtest.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class AppAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils = new JwtUtils();

    public AppAuthenticationFilter(AuthenticationManager authenticationManager){
        this.setFilterProcessesUrl("/api/v1/authenticate");
        this.authenticationManager = authenticationManager;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        LoginRequest loginRequest;
        try{
            loginRequest = new ObjectMapper().readValue(request.getInputStream(),LoginRequest.class);
        } catch (Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword());

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();

        String accessToken = jwtUtils.generateAccessToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);

        Map<String,Object> tokens = new HashMap<>();
        tokens.put("access_token",accessToken);
        tokens.put("refresh_token",refreshToken);
        response.setContentType("application/json");

        new ObjectMapper().writeValue(response.getOutputStream(), new ApiResponse(
                HttpStatus.OK.value(),
                "User Authentication Successful!",
                tokens
        ));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        new ObjectMapper().writeValue(response.getOutputStream(), new ApiErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                "Authentication Failed!"
        ));
    }
}
