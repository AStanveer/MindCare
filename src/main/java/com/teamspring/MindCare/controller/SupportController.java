package com.teamspring.MindCare.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.teamspring.MindCare.dto.ChatRequest;
import com.teamspring.MindCare.dto.ChatResponse;
import com.teamspring.MindCare.dto.PostDTO;
import com.teamspring.MindCare.model.User;
import com.teamspring.MindCare.service.ChatbotService;
import com.teamspring.MindCare.service.SupportService;
import com.teamspring.MindCare.service.FeatureUsageService;
import com.teamspring.MindCare.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/mindcare/")
public class SupportController {

    @Autowired
    private SupportService supportService;

    @Autowired
    private ChatbotService chatbotService;
    
    @Autowired
    private FeatureUsageService featureUsageService;

    @Autowired
    private UserService userService;

    private User getLoggedInUser(Principal principal) {
        return userService.getUserByEmail(principal.getName());
    }
    
    @GetMapping("/peer-support")
    public String peerSupport(Model model, @RequestParam(required = false) String tag, Principal principal) {

        User currentUser = getLoggedInUser(principal);
        
        // Track feature usage when user accesses peer support
        featureUsageService.incrementPeerSupportUsage(currentUser.getId());

        List<PostDTO> posts = supportService.getAllPosts(tag, currentUser);

        model.addAttribute("username", currentUser.getFullName());
        model.addAttribute("posts", posts);
        model.addAttribute("activeTag", tag);
                
        model.addAttribute("user", currentUser);
        
        return "support/peer-support-forum";
    }

    @PostMapping("/post/create")
    public String createPost(
        @RequestParam String title,
        @RequestParam String content,
        @RequestParam(defaultValue="General") String tag,
        Principal principal
    ) {
        User currentUser = getLoggedInUser(principal);
        supportService.createPost(title, content, tag, currentUser);        
        return "redirect:/mindcare/peer-support";
    }

    @PostMapping("/reply/create")
    public String createReply(
        @RequestParam Long postId,
        @RequestParam String content,
        Principal principal
    ) {
        User currentUser = getLoggedInUser(principal);
        supportService.createReply(postId, content, currentUser);
        return "redirect:/mindcare/peer-support";
    }

    @PostMapping("/post/{id}/like")
    @ResponseBody 
    public SupportService.LikeResponse likePost(@PathVariable Long id, Principal principal) {
        User currentUser = getLoggedInUser(principal);
        return supportService.toggleLike(id, currentUser);
    }

    @PostMapping("/reply/{id}/like")
    @ResponseBody
    public SupportService.LikeResponse likeReply(@PathVariable Long id, Principal principal) {
        User currentUser = getLoggedInUser(principal);
        return supportService.toggleReplyLike(id, currentUser);
    }

    @GetMapping("/support-chat")
    public String supportChat(Model model, Principal principal) {
        // Track feature usage when user accesses support chat
        User currentUser = getLoggedInUser(principal);

        featureUsageService.incrementPeerSupportUsage(currentUser.getId());
        
        model.addAttribute("username", currentUser.getFullName());
        model.addAttribute("user", currentUser);
        return "support/support-chatbot";
    }

    @PostMapping("api/chat")
    @ResponseBody
    public ChatResponse chatWithBot(@RequestBody ChatRequest request, HttpSession session) {
        String botReply = chatbotService.getResponse(request.getMessage(), session);
        
        // JSON
        return new ChatResponse(botReply);
    }

}