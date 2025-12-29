package com.teamspring.MindCare.dto;

public class ReplyDTO {
    private Long id;
    private boolean isLikedByCurrentUser;
    private String initials;
    private String authorName;
    private String timeAgo;
    private String content;
    private int likesCount;
    private String avatarColorClass;

    public ReplyDTO(Long id, boolean isLikedByCurrentUser, String initials, String authorName, String timeAgo, String content, int likesCount,
            String avatarColorClass) {
        this.id = id;
        this.isLikedByCurrentUser = isLikedByCurrentUser;
        this.initials = initials;
        this.authorName = authorName;
        this.timeAgo = timeAgo;
        this.content = content;
        this.likesCount = likesCount;
        this.avatarColorClass = avatarColorClass;
    }

    public Long getId() {
        return id;
    }

    public boolean getIsLikedByCurrentUser() {
        return isLikedByCurrentUser;
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

    public String getContent() {
        return content;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public String getAvatarColorClass() {
        return avatarColorClass;
    }
}
