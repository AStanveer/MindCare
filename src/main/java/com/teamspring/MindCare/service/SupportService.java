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
import com.teamspring.MindCare.model.User;
import com.teamspring.MindCare.repository.PostLikeRepository;
import com.teamspring.MindCare.repository.PostRepository;
import com.teamspring.MindCare.repository.ReplyLikeRepository;
import com.teamspring.MindCare.repository.ReplyRepository;

@Service
public class SupportService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private ReplyLikeRepository replyLikeRepository;

    @Transactional(readOnly = true)
    public List<PostDTO> getAllPosts(String tag, User currentUser) {
        List<Post> rawPosts;

        if (tag != null && !tag.isEmpty()) {
            rawPosts = postRepository.findByTagOrderByCreatedAtDesc(tag);
        } else {
            rawPosts = postRepository.findAllByOrderByCreatedAtDesc();
        }

        // Map rawPosts to PostDTOs
        return rawPosts.stream()
            .map(post -> convertToPostDTO(post, currentUser)) 
            .collect(Collectors.toList());
    }

    private PostDTO convertToPostDTO(Post post, User currentUser) {
        Long id = post.getId();
        
        String authorName = (post.getAuthor() != null) ? post.getAuthor().getFullName() : "Unknown";
        String timeAgo = calculateTimeAgo(post.getCreatedAt());
        String initials = generateInitials(authorName);

        // Check if CURRENT user liked this post
        boolean isLikedByCurrentUser = post.getLikes().stream()
            .anyMatch(like -> like.getUser().getId().equals(currentUser.getId()));

        List<ReplyDTO> replyDTOs = new ArrayList<>();
        if(post.getReplies() != null) {
            replyDTOs = post.getReplies().stream()
                    .map(reply -> convertToReplyDTO(reply, currentUser))
                    .collect(Collectors.toList());
        }

        String tagClass = "tag-purple";
        if ("Anxiety".equalsIgnoreCase(post.getTag())) tagClass = "tag-blue";
        else if ("Sleep".equalsIgnoreCase(post.getTag())) tagClass = "tag-cyan";

        return new PostDTO(
            id, initials, authorName, timeAgo, post.getTitle(), post.getContent(),
            post.getLikesCount(), replyDTOs.size(), tagClass, post.getTag(),
            "avatar-purple", false, replyDTOs, isLikedByCurrentUser
        );
    }

    private ReplyDTO convertToReplyDTO(Reply reply, User currentUser) {
        String authorName = (reply.getAuthor() != null) ? reply.getAuthor().getFullName() : "Unknown";
        String timeAgo = calculateTimeAgo(reply.getCreatedAt());
        String initials = generateInitials(authorName);
        
        boolean isLiked = reply.getLikes().stream()
            .anyMatch(like -> like.getUser().getId().equals(currentUser.getId()));
        
        return new ReplyDTO(
            reply.getId(), isLiked, initials, authorName, timeAgo,
            reply.getContent(), reply.getLikesCount(), "avatar-purple"
        );
    }

    public void createPost(String title, String content, String tagName, User author) {
        Post newPost = new Post();
        newPost.setAuthor(author);
        newPost.setTitle(title);
        newPost.setContent(content);
        newPost.setTag(tagName);
        postRepository.save(newPost);
    }

    public void createReply(Long postId, String content, User author) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) throw new IllegalArgumentException("Post not found");

        Reply newReply = new Reply();
        newReply.setPost(post);
        newReply.setAuthor(author);
        newReply.setContent(content);
        replyRepository.save(newReply);
    }
    
    @Transactional
    public LikeResponse toggleLike(Long postId, User user) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) throw new IllegalArgumentException("Post not found");

        Optional<PostLike> existingLike = postLikeRepository.findByPostAndUser(post, user);

        boolean isLiked;
        if(existingLike.isPresent()) {
            postLikeRepository.delete(existingLike.get());
            post.setLikesCount(post.getLikesCount() - 1);
            isLiked = false;
        } else {
            postLikeRepository.save(new PostLike(post, user));
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
    public LikeResponse toggleReplyLike(Long replyId, User user) {
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
}
