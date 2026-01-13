package com.teamspring.MindCare.controller;

import com.teamspring.MindCare.model.User;
import com.teamspring.MindCare.service.FileStorageService;
import com.teamspring.MindCare.service.UserService;
import jakarta.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/mindcare/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileStorageService fileStorageService;

    /* =========================
       VIEW PROFILE
       ========================= */
    @GetMapping("/view")
    public String viewProfile(Model model, Principal principal, HttpSession session) {

        Optional<User> userOpt = getCurrentUser(principal, session);
        if (userOpt.isEmpty()) {
            return "redirect:/auth/login";
        }

        model.addAttribute("user", userOpt.get());
        model.addAttribute("activePage", "PROFILE");
        return "profile/profile-view";
    }

    /* =========================
       EDIT PROFILE
       ========================= */
    @GetMapping("/edit")
    public String editProfile(Model model, Principal principal, HttpSession session) {

        Optional<User> userOpt = getCurrentUser(principal, session);
        if (userOpt.isEmpty()) {
            return "redirect:/auth/login";
        }

        model.addAttribute("user", userOpt.get());
        model.addAttribute("activePage", "PROFILE");
        return "profile/profile-edit";
    }

    /* =========================
       UPDATE PROFILE
       ========================= */
    @PostMapping("/update")
    public String updateProfile(
            @ModelAttribute("user") User updatedUser,
            BindingResult bindingResult,
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String licenseNumber,
            @RequestParam(required = false) String experience,
            Principal principal,
            HttpSession session,
            RedirectAttributes redirectAttributes,
            Model model) {

        Optional<User> userOpt = getCurrentUser(principal, session);
        if (userOpt.isEmpty()) {
            return "redirect:/auth/login";
        }

        User currentUser = userOpt.get();

        try {
            /* ===== Common Fields ===== */
            currentUser.setFullName(updatedUser.getFullName());
            currentUser.setPhone(updatedUser.getPhone());
            currentUser.setLocation(updatedUser.getLocation());
            currentUser.setDepartment(updatedUser.getDepartment());

            /* ===== Role-Specific Fields ===== */
            switch (currentUser.getRole()) {

                case STUDENT -> currentUser.setYear(year);

                case PROFESSIONAL -> {
                    currentUser.setBio(updatedUser.getBio());
                    currentUser.setLicenseNumber(licenseNumber);
                    currentUser.setExperience(experience);
                }

                case ADMIN -> {
                    // admins only update common fields
                }
            }

            /* ===== Save to DB ===== */
            User savedUser = userService.updateUser(currentUser.getId(), currentUser);

            /* ===== Refresh Session ===== */
            refreshSession(session, savedUser);

            redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
            return "redirect:/mindcare/profile/view";

        } catch (Exception e) {
            model.addAttribute("user", currentUser);
            model.addAttribute("error", "Error updating profile");
            model.addAttribute("activePage", "PROFILE");
            return "profile/profile-edit";
        }
    }

    /* =========================
    CHANGE PASSWORD
    ========================= */
    @GetMapping("/change-password")
    public String showChangePasswordPage(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("activePage", "PROFILE");
        return "profile/change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            Principal principal,
            HttpSession session,
            RedirectAttributes redirectAttributes,
            Model model) {

        User user = userService.getUserByEmail(principal.getName());
        
        // Validation
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            model.addAttribute("activePage", "PROFILE");
            return "profile/change-password";
        }

        if (newPassword.length() < 6) {
            model.addAttribute("error", "Password must be at least 6 characters");
            model.addAttribute("activePage", "PROFILE");
            return "profile/change-password";
        }

        try {
            userService.changePassword(user.getId(), currentPassword, newPassword);
            redirectAttributes.addFlashAttribute("success", "Password changed successfully!");
            return "redirect:/mindcare/profile/view";

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("activePage", "PROFILE");
            return "profile/change-password";
        }
    }

    /* =========================
       SESSION + AUTH HELPERS
       ========================= */
    private Optional<User> getCurrentUser(Principal principal, HttpSession session) {

        if (principal == null) {
            return Optional.empty();
        }

        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser != null) {
            return Optional.of(sessionUser);
        }

        Optional<User> dbUser = userService.findByEmail(principal.getName());
        dbUser.ifPresent(user -> refreshSession(session, user));

        return dbUser;
    }

    private void refreshSession(HttpSession session, User user) {
        session.setAttribute("user", user);
        session.setAttribute("userId", user.getId());
        session.setAttribute("userName", user.getFullName());
        session.setAttribute("userEmail", user.getEmail());
        session.setAttribute("userRole", user.getRole());
    }
}
