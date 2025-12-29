package com.teamspring.MindCare.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamspring.MindCare.dto.PostDTO;
import com.teamspring.MindCare.dto.ReplyDTO;
import com.teamspring.MindCare.model.Post;
import com.teamspring.MindCare.model.PostLike;
import com.teamspring.MindCare.model.Reply;
import com.teamspring.MindCare.model.ReplyLike;
import com.teamspring.MindCare.model.UserTemp;
import com.teamspring.MindCare.repository.PostLikeRepository;
import com.teamspring.MindCare.repository.PostRepository;
import com.teamspring.MindCare.repository.ReplyLikeRepository;
import com.teamspring.MindCare.repository.ReplyRepository;
import com.teamspring.MindCare.repository.UserTempRepository;

@Service
public class SupportService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserTempRepository userTempRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private ReplyLikeRepository replyLikeRepository;


    public List<PostDTO> getAllPosts(String tag) {
        List<Post> rawPosts;

        if (tag != null && !tag.isEmpty()) {
            rawPosts = postRepository.findByTagOrderByCreatedAtDesc(tag);
        } else {
            rawPosts = postRepository.findAllByOrderByCreatedAtDesc();
        }

        // Map rawPosts to PostDTOs
        return rawPosts.stream()
                .map(this::convertToPostDTO)
                .collect(Collectors.toList());
    }

    private PostDTO convertToPostDTO(Post post) {
        // Implement the conversion logic
        Long id = post.getId();
        UserTemp currentUser = getSimulatedUser();

        String authorName = (post.getAuthor() != null)? post.getAuthor().getFullName() : "Unknown";

        String timeAgo = calculateTimeAgo(post.getCreatedAt());
        String initials = generateInitials(authorName);

        boolean isLikedByCurrentUser = post.getLikes().stream()
        .anyMatch(like -> like.getUser().getId().equals(currentUser.getId()));

        List<ReplyDTO> replyDTOs = new ArrayList<>();

        if(post.getReplies() != null) {
            replyDTOs = post.getReplies().stream()
                    .map(this::convertToReplyDTO)
                    .collect(Collectors.toList());
        }

        String tagClass = "tag-purple";
        String tagName = post.getTag();
        
        if ("Anxiety".equalsIgnoreCase(tagName)) tagClass = "tag-blue";
        else if ("Sleep".equalsIgnoreCase(tagName)) tagClass = "tag-cyan";

        return new PostDTO(
            id,
            initials,
            authorName,
            timeAgo,
            post.getTitle(),
            post.getContent(),
            post.getLikesCount(),
            replyDTOs.size(),
            tagClass,
            tagName,
            "avatar-purple",// Randomize this later
            false,
            replyDTOs,
            isLikedByCurrentUser
        );
    }

    private ReplyDTO convertToReplyDTO(Reply reply) {
        UserTemp currentUser = getSimulatedUser();
        String authorName = (reply.getAuthor() != null) ? reply.getAuthor().getFullName() : "Unknown";
        String timeAgo = calculateTimeAgo(reply.getCreatedAt());
        String initials = generateInitials(authorName);
        
        boolean isLiked = reply.getLikes().stream()
            .anyMatch(like -> like.getUser().getId().equals(currentUser.getId()));
        
        return new ReplyDTO(
            reply.getId(),
            isLiked,
            initials,
            authorName,
            timeAgo,
            reply.getContent(),
            reply.getLikesCount(),
            "avatar-purple"
        );
    }

    public void createPost(String title, String content, String tagName) {
        UserTemp currentUser = getSimulatedUser();

        Post newPost = new Post();
        newPost.setAuthor(currentUser);
        newPost.setTitle(title);
        newPost.setContent(content);
        newPost.setTag(tagName);

        postRepository.save(newPost);
    }

    public void createReply(Long postId, String content) {
        UserTemp currentUser = getSimulatedUser();

        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) throw new IllegalArgumentException("Post not found");

        Reply newReply = new Reply();
        newReply.setPost(post);
        newReply.setAuthor(currentUser);
        newReply.setContent(content);

        replyRepository.save(newReply);
    }
    
    @Transactional
    public LikeResponse toggleLike(Long postId) {
        UserTemp user = getSimulatedUser();
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) throw new IllegalArgumentException("Post not found");

        Optional<PostLike> existingLike = postLikeRepository.findByPostAndUser(post, user);

        boolean isLiked;

        if(existingLike.isPresent()) {
            postLikeRepository.delete(existingLike.get());
            post.setLikesCount(post.getLikesCount() - 1);
            isLiked = false;
        } else {
            PostLike newLike = new PostLike(post, user);
            postLikeRepository.save(newLike);
            post.setLikesCount(post.getLikesCount() + 1);
            isLiked = true;
        }

        return new LikeResponse(post.getLikesCount(), isLiked);
    }

    public record LikeResponse(int newCount, boolean isLiked) {}

    @Transactional
    public LikeResponse likeReply(Long replyId) {
        Reply reply = replyRepository.findById(replyId).orElse(null);
        if (reply == null) throw new IllegalArgumentException("Reply not found");

        reply.setLikesCount(reply.getLikesCount() + 1);
        replyRepository.save(reply);
        return new LikeResponse(reply.getLikesCount(), true);
    }

    @Transactional
    public LikeResponse toggleReplyLike(Long replyId) {
        UserTemp user = getSimulatedUser();
        Reply reply = replyRepository.findById(replyId).orElse(null);
        if (reply == null) throw new IllegalArgumentException("Reply not found");

        Optional<ReplyLike> existingLike = replyLikeRepository.findByReplyAndUser(reply, user);

        boolean isLiked;

        if (existingLike.isPresent()) {
            replyLikeRepository.delete(existingLike.get());
            reply.setLikesCount(reply.getLikesCount() - 1);
            isLiked = false;
        } else {
            replyLikeRepository.save(new ReplyLike(reply, user));
            reply.setLikesCount(reply.getLikesCount() + 1);
            isLiked = true;
        }
        
        replyRepository.save(reply);
        return new LikeResponse(reply.getLikesCount(), isLiked);
    }

    // --- HELPER FUNCTIONS ---

    private String generateInitials(String name) {
        if (name == null || name.isEmpty()) return "?";
        String[] parts = name.split("\\s+");
        if (parts.length >= 2) {
            return (parts[0].substring(0, 1) + parts[1].substring(0, 1)).toUpperCase();
        }
        return name.substring(0, 1).toUpperCase();
    }

    private String calculateTimeAgo(LocalDateTime createdTime) {
        if (createdTime == null) return "Just now";
        Duration duration = Duration.between(createdTime, LocalDateTime.now());
        
        long minutes = duration.toMinutes();
        if (minutes < 1) return "Just now";
        if (minutes < 60) return minutes + " mins ago";
        
        long hours = duration.toHours();
        if (hours < 24) return hours + " hours ago";
        
        long days = duration.toDays();
        return days + " days ago";
    }

    private UserTemp getSimulatedUser() {
        return userTempRepository.findAll().stream()
            .filter(u -> u.getFullName().equals("Moaz"))
            .findFirst()
            .orElseGet(() -> {
                UserTemp moaz = new UserTemp("Moaz", "moaz@mindcare.com", "pass");
                return userTempRepository.save(moaz);
            });
    }
}
