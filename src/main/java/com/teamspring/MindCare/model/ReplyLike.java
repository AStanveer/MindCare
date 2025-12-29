package com.teamspring.MindCare.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reply_likes")
public class ReplyLike {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reply_id")
    private Reply reply;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserTemp user;

    public ReplyLike() {}

    public ReplyLike(Reply reply, UserTemp user) {
        this.reply = reply;
        this.user = user;
    }

    public Reply getReply() { return reply; }
    public UserTemp getUser() { return user; }
}