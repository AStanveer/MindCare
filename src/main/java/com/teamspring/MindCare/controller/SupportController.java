package com.teamspring.MindCare.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.teamspring.MindCare.service.ChatbotService;
import com.teamspring.MindCare.service.SupportService;
import com.teamspring.MindCare.service.FeatureUsageService;

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
    
    private static final Long DUMMY_USER_ID = 1L;

    @GetMapping("/peer-support")
    public String peerSupport(Model model, @RequestParam(required = false) String tag) {
        
        // Track feature usage when user accesses peer support
        featureUsageService.incrementPeerSupportUsage(DUMMY_USER_ID);

        List<PostDTO> posts = supportService.getAllPosts(tag);

        model.addAttribute("username", "Moaz");
        model.addAttribute("posts", posts);
        model.addAttribute("activeTag", tag);
        
        return "support/peer-support-forum";
    }

    @PostMapping("/post/create")
    public String createPost(
        @RequestParam String title,
        @RequestParam String content,
        @RequestParam(defaultValue="General") String tag
    ) {
        supportService.createPost(title, content, tag);
        return "redirect:/mindcare/peer-support";
    }

    @PostMapping("reply/create")
    public String createReply(
        @RequestParam Long postId,
        @RequestParam String content
    ) {
        supportService.createReply(postId, content);
        return "redirect:/mindcare/peer-support";
    }

    @PostMapping("/post/{id}/like")
    @ResponseBody 
    public SupportService.LikeResponse likePost(@PathVariable Long id) {
        return supportService.toggleLike(id);
    }

    @PostMapping("/reply/{id}/like")
    @ResponseBody
    public SupportService.LikeResponse likeReply(@PathVariable Long id) {
        return supportService.toggleReplyLike(id);
    }

    @GetMapping("/support-chat")
    public String supportChat(Model model) {
        // Track feature usage when user accesses support chat
        featureUsageService.incrementPeerSupportUsage(DUMMY_USER_ID);
        
        model.addAttribute("username", "Moaz");
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