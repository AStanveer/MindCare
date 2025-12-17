package com.teamspring.MindCare.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mindcare/student")
public class PeerSupportController {

    @GetMapping("/peer-support")
    public String peerSupport(Model model) {
        
        List<Reply> examReplies = List.of(
            new Reply("CT", "Casey T.", "1 hour ago", "I find breaking study sessions into 25-minute chunks really helpful. Take short breaks between!", 3, "avatar-purple"),
            new Reply("ML", "Morgan L.", "45 mins ago", "Deep breathing exercises before each study session changed everything for me. Try the 4-7-8 technique!", 5, "avatar-purple"),
            new Reply("TR", "Taylor R.", "30 mins ago", "Remember to get enough sleep! I used to pull all-nighters but found I retained more with proper rest.", 4, "avatar-purple")
        );

        List<Post> posts = new ArrayList<>();
        
        // Post 1: Open with replies (Matches Screenshot 2)
        posts.add(new Post(
            "AM", "Alex M.", "2 hours ago", "Managing exam stress", 
            "Finals are coming up and I'm feeling overwhelmed. Any tips on staying calm during this time?",
            12, 3, "tag-purple", "Academic Stress", "avatar-purple",
            false,
            examReplies
        ));

        // Post 2: Collapsed
        posts.add(new Post(
            "JP", "Jordan P.", "5 hours ago", "Meditation helped me!", 
            "Just wanted to share that starting a daily meditation practice has really improved my mood. Highly recommend!",
            24, 0, "tag-purple", "Self-Care", "avatar-purple",
            false,
            null
        ));
        
        // Post 3: Collapsed
        posts.add(new Post(
            "SK", "Sam K.", "1 day ago", "Sleep schedule tips?", 
            "I've been having trouble maintaining a consistent sleep schedule. What works for you?",
            8, 0, "tag-purple", "Sleep", "avatar-purple",
            false,
            null
        ));

        model.addAttribute("username", "Moaz");
        model.addAttribute("posts", posts);
        
        return "support/peer-support-forum";
    }

    public static class Post {
        public String initials;
        public String authorName;
        public String timeAgo;
        public String title;
        public String content;
        public int likes;
        public int repliesCount;
        public String tagClass;
        public String tagName;
        public String avatarColorClass;
        public boolean showReplies;
        public List<Reply> replies;

        public Post(String initials, String authorName, String timeAgo, String title, String content, int likes, int repliesCount, String tagClass, String tagName, String avatarColorClass, boolean showReplies, List<Reply> replies) {
            this.initials = initials;
            this.authorName = authorName;
            this.timeAgo = timeAgo;
            this.title = title;
            this.content = content;
            this.likes = likes;
            this.repliesCount = repliesCount;
            this.tagClass = tagClass;
            this.tagName = tagName;
            this.avatarColorClass = avatarColorClass;
            this.showReplies = showReplies;
            this.replies = replies;
        }
    }

    public static class Reply {
        public String initials;
        public String authorName;
        public String timeAgo;
        public String content;
        public int likes;
        public String avatarColorClass;

        public Reply(String initials, String authorName, String timeAgo, String content, int likes, String avatarColorClass) {
            this.initials = initials;
            this.authorName = authorName;
            this.timeAgo = timeAgo;
            this.content = content;
            this.likes = likes;
            this.avatarColorClass = avatarColorClass;
        }
    }
}