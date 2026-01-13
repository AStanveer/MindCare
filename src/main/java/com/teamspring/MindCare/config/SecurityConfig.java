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

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authenticationProvider(authenticationProvider())

            // CSRF protection
            .csrf(csrf -> csrf
                .ignoringRequestMatchers(
                    "/auth/register",
                    "/auth/login"
                )
            )

            .authorizeHttpRequests(authorize -> authorize
                // Public resources
                .requestMatchers(
                    "/auth/**",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/fonts/**",
                    "/error",
                    "/",
                    "/index"
                ).permitAll()

                // Role-based dashboards
                .requestMatchers("/mindcare/dashboard/student", "/mindcare/student/**")
                    .hasRole("STUDENT")
                .requestMatchers("/mindcare/dashboard/professional", "/mindcare/professional/**")
                    .hasRole("PROFESSIONAL")
                .requestMatchers("/mindcare/admin/**", "/mindcare/dashboard/admin")
                    .hasRole("ADMIN")

                // Professional content area (your controller is under /professional/**)
                .requestMatchers("/professional/**").hasRole("PROFESSIONAL")

                // General authenticated endpoints
                .requestMatchers("/mindcare/dashboard", "/profile/**").authenticated()

                // Everything else requires authentication
                .anyRequest().authenticated()
            )

            .formLogin(form -> form
                .loginPage("/auth/login")
                .loginProcessingUrl("/auth/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .successHandler(successHandler)
                .failureUrl("/auth/login?error=true")
                .permitAll()
            )

            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/auth/login?logout=true")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )

            .sessionManagement(session -> session
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .expiredUrl("/auth/login?expired=true")
            )

            .rememberMe(remember -> remember
                .key("mindcare-secure-remember-key-2024")
                .tokenValiditySeconds(7 * 24 * 60 * 60)   // 7 days
                .rememberMeParameter("remember-me")
                .userDetailsService(userDetailsService)
            );

        return http.build();
    }
}
