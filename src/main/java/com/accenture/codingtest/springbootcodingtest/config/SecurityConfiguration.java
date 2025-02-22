package com.accenture.codingtest.springbootcodingtest.config;

import com.accenture.codingtest.springbootcodingtest.constants.Role;
import com.accenture.codingtest.springbootcodingtest.filters.AppAuthenticationFilter;
import com.accenture.codingtest.springbootcodingtest.filters.AppAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true,jsr250Enabled = true)
public class SecurityConfiguration {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder bCryptPasswordEncoder,
                                                       UserDetailsService userDetailsService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder)
                .and().build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = authenticationManager(http,passwordEncoder(),userDetailsService);

        http.csrf().disable();
        http.cors();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests().antMatchers("/api/v1/users/**")
                .hasAuthority(Role.ADMIN.name());

        http.authorizeRequests().antMatchers(HttpMethod.POST,"/api/v1/projects")
                .hasAnyAuthority(Role.ADMIN.name(),Role.PRODUCT_OWNER.name());
        http.authorizeRequests().antMatchers(HttpMethod.GET,"/api/v1/projects/**")
                .hasAnyAuthority(Role.ADMIN.name(),Role.PRODUCT_OWNER.name(),Role.USER.name());
        http.authorizeRequests().antMatchers(HttpMethod.PUT,"/api/v1/projects/**")
                .hasAnyAuthority(Role.ADMIN.name(),Role.PRODUCT_OWNER.name(),Role.USER.name());
        http.authorizeRequests().antMatchers(HttpMethod.PATCH,"/api/v1/projects/**")
                .hasAnyAuthority(Role.ADMIN.name(),Role.PRODUCT_OWNER.name(),Role.USER.name());

        http.authorizeRequests().antMatchers(HttpMethod.GET,"/api/v1/task/**")
                .hasAnyAuthority(Role.ADMIN.name(),Role.PRODUCT_OWNER.name(),Role.USER.name());
        http.authorizeRequests().antMatchers(HttpMethod.POST,"/api/v1/task/**")
                .hasAnyAuthority(Role.ADMIN.name(),Role.PRODUCT_OWNER.name());
        http.authorizeRequests().antMatchers(HttpMethod.PATCH,"/api/v1/tasks/assign/**")
                .hasAnyAuthority(Role.PRODUCT_OWNER.name(),Role.ADMIN.name());
        http.authorizeRequests().antMatchers(HttpMethod.PATCH,"/api/v1/tasks/")
                .hasAnyAuthority(Role.PRODUCT_OWNER.name(),Role.ADMIN.name(), Role.USER.name());


        http.authenticationManager(authenticationManager);
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilterBefore(new AppAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilter(new AppAuthenticationFilter(authenticationManager));
        return http.build();
    }
}
