package com.teamspring.MindCare.controller;

import com.teamspring.MindCare.model.User;
import com.teamspring.MindCare.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Authentication Controller
 * Handles login, registration, and password management with Spring Security integration
 */
@Controller
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private SecurityContextRepository securityContextRepository;
    
    @GetMapping("/login")
    public String showLoginPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "expired", required = false) String expired,
            Model model) {
        
        if (error != null) {
            model.addAttribute("error", "Invalid email or password");
        }
        
        if (logout != null) {
            model.addAttribute("success", "You have been logged out successfully");
        }
        
        if (expired != null) {
            model.addAttribute("error", "Your session has expired. Please login again");
        }
        
        model.addAttribute("user", new User());
        return "auth/login";
    }
    
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }
    
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes,
                          HttpServletRequest request,
                          HttpServletResponse response,
                          Model model) {
        
        if (bindingResult.hasErrors()) {
            System.out.println("VALIDATION ERRORS: " + bindingResult.getAllErrors());
            return "auth/register";
        }
        
        // Store the raw password before it gets hashed
        String rawPassword = user.getPassword();
        
        try {

            System.out.println("Attempting to register user with Role: " + user.getRole());

            // Register user (password will be hashed and saved to database)
            User registeredUser = userService.registerUser(user);
            
            System.out.println("User saved to DB with Role: " + registeredUser.getRole());
            // Auto-login: Authenticate the user programmatically
            UsernamePasswordAuthenticationToken authToken = 
                new UsernamePasswordAuthenticationToken(
                    registeredUser.getEmail(), 
                    rawPassword
                );
            
            Authentication authentication = authenticationManager.authenticate(authToken);
            
            // Set the authentication in SecurityContext
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);
            
            // Persist the SecurityContext to the session
            securityContextRepository.saveContext(securityContext, request, response);
            
            redirectAttributes.addFlashAttribute("success", 
                "Registration successful! Welcome to MindCare, " + registeredUser.getFullName() + "!");
            
            // Role-based redirection
            switch (registeredUser.getRole()) {
                case STUDENT:
                    return "redirect:/mindcare/dashboard/student";
                case PROFESSIONAL:
                    return "redirect:/mindcare/dashboard/professional";
                case ADMIN:
                    return "redirect:/mindcare/admin/dashboard";
                default:
                    return "redirect:/mindcare/dashboard";
            }
            
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/register";
        }
    }
    
    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return "auth/forgot-password";
    }
    
    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email,
                                RedirectAttributes redirectAttributes) {
        try {
            userService.initiatePasswordReset(email);
            redirectAttributes.addFlashAttribute("success", 
                "Password reset instructions have been sent to your email");
            return "redirect:/auth/login";
            
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/auth/forgot-password";
        }
    }
    
    @GetMapping("/reset-password")
    public String showResetPasswordPage(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        return "auth/reset-password";
    }
    
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String token,
                               @RequestParam String newPassword,
                               @RequestParam String confirmPassword,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            model.addAttribute("token", token);
            return "auth/reset-password";
        }
        
        try {
            // Password will be hashed by UserService using BCrypt
            userService.resetPassword(token, newPassword);
            redirectAttributes.addFlashAttribute("success", 
                "Password reset successful! Please login with your new password.");
            return "redirect:/auth/login";
            
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("token", token);
            return "auth/reset-password";
        }
    }
}