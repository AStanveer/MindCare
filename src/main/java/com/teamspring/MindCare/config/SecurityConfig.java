package com.teamspring.MindCare.config;

import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Spring Security Configuration for MindCare Platform
 * Implements BCrypt password hashing and role-based access control
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;
    
    /**
     * BCrypt Password Encoder Bean
     * Strength 12 provides excellent security while maintaining performance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
    
    /**
     * Security Context Repository Bean
     * Manages the security context in HTTP session
     */
    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }
    
    /**
     * Authentication Provider
     * Connects UserDetailsService with PasswordEncoder for authentication
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    
    /**
     * Authentication Manager Bean
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    
    /**
     * Security Filter Chain Configuration
     * Defines authorization rules and authentication mechanisms
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authenticationProvider(authenticationProvider())
            
            // CSRF protection (enabled for production security)
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/auth/register", "/auth/login") // Allow these endpoints
            )
            
            // Authorization rules
            .authorizeHttpRequests(authorize -> authorize
                // Public endpoints
                .requestMatchers("/auth/**", "/css/**", "/js/**", "/images/**", 
                                "/fonts/**", "/error", "/", "/index").permitAll()
                
                // Role-based dashboard access
                .requestMatchers("/mindcare/dashboard/student", "/mindcare/student/**")
                    .hasRole("STUDENT")
                .requestMatchers("/mindcare/dashboard/professional", "/mindcare/professional/**")
                    .hasRole("PROFESSIONAL")
                .requestMatchers("/mindcare/admin/**", "/mindcare/dashboard/admin")
                    .hasRole("ADMIN")
                
                // General authenticated endpoints
                .requestMatchers("/mindcare/dashboard", "/profile/**").authenticated()
                
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            
            // Form login configuration
            .formLogin(form -> form
                .loginPage("/auth/login")
                .loginProcessingUrl("/auth/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .successHandler(successHandler) // Use custom success handler for role-based redirect
                .failureUrl("/auth/login?error=true")
                .permitAll()
            )
            
            // Logout configuration
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/auth/login?logout=true")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            
            // Session management
            .sessionManagement(session -> session
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .expiredUrl("/auth/login?expired=true")
            )
            
            // Remember me functionality
            .rememberMe(remember -> remember
                .key("mindcare-secure-remember-key-2024")
                .tokenValiditySeconds(7 * 24 * 60 * 60) // 7 days
                .rememberMeParameter("remember-me")
                .userDetailsService(userDetailsService)
            );
        
        return http.build();
    }
}