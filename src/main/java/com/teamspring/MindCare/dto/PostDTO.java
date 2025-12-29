package com.teamspring.MindCare.dto;

import java.util.List;

public class PostDTO {
    private Long id;
    private String initials;
    private String authorName;
    private String timeAgo;
    private String title;
    private String content;
    private int likesCount;
    private int repliesCount;
    private String tagClass;
    private String tagName;
    private String avatarColorClass;
    private boolean showReplies;
    private List<ReplyDTO> replies;
    private boolean isLikedByCurrentUser;

    public PostDTO(Long id, String initials, String authorName, String timeAgo, String title, String content, int likesCount, int repliesCount, String tagClass, String tagName, String avatarColorClass, boolean showReplies, List<ReplyDTO> replies, boolean isLikedByCurrentUser) {
        this.id = id;
        this.initials = initials;
        this.authorName = authorName;
        this.timeAgo = timeAgo;
        this.title = title;
        this.content = content;
        this.likesCount = likesCount;
        this.repliesCount = repliesCount;
        this.tagClass = tagClass;
        this.tagName = tagName;
        this.avatarColorClass = avatarColorClass;
        this.showReplies = showReplies;
        this.replies = replies;
        this.isLikedByCurrentUser = isLikedByCurrentUser;
    }

    public Long getId() {
        return id;
    }

    public String getInitials() {
        return initials;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public int getRepliesCount() {
        return repliesCount;
    }

    public String getTagClass() {
        return tagClass;
    }

    public String getTagName() {
        return tagName;
    }

    public String getAvatarColorClass() {
        return avatarColorClass;
    }

    public boolean isShowReplies() {
        return showReplies;
    }

    public List<ReplyDTO> getReplies() {
        return replies;
    }

    public boolean isLikedByCurrentUser() {
        return isLikedByCurrentUser;
    }
}
