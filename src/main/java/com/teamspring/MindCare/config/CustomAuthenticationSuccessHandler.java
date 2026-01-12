package com.teamspring.MindCare.config;

import com.teamspring.MindCare.model.User;
import com.teamspring.MindCare.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Custom Authentication Success Handler
 * Handles post-login actions: session setup, last login update, and role-based redirection
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                       HttpServletResponse response,
                                       Authentication authentication) throws IOException, ServletException {
        
        // Get user email from authentication
        String email = authentication.getName();
        
        // Load user from database
        User user = userRepository.findByEmail(email).orElse(null);
        
        if (user != null) {
            // Update last active time
            user.setLastActive(LocalDateTime.now());
            userRepository.save(user);
            
            // Store user info in session
            HttpSession session = request.getSession();
            session.setAttribute("userId", user.getId());
            session.setAttribute("userName", user.getFullName());
            session.setAttribute("userEmail", user.getEmail());
            session.setAttribute("userRole", user.getRole());
            session.setAttribute("user", user);
        }
        
        // Get user roles
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        
        // Role-based redirection with correct mappings
        String redirectUrl = "/mindcare/dashboard";
        
        if (roles.contains("ROLE_STUDENT")) {
            redirectUrl = "/mindcare/dashboard/student";
        } else if (roles.contains("ROLE_PROFESSIONAL")) {
            redirectUrl = "/mindcare/dashboard/professional";
        } else if (roles.contains("ROLE_ADMIN")) {
            redirectUrl = "/mindcare/admin/dashboard";
        }
        
        response.sendRedirect(redirectUrl);
    }
}