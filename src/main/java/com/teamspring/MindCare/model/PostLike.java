package com.teamspring.MindCare.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "post_likes")
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserTemp user;

    public PostLike() {}

    public PostLike(Post post, UserTemp user) {
        this.post = post;
        this.user = user;
    }
    
    // Getters and Setters
    public Post getPost() { return post; }
    public UserTemp getUser() { return user; }
}

