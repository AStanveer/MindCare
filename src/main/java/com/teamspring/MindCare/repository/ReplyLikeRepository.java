package com.teamspring.MindCare.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teamspring.MindCare.model.Reply;
import com.teamspring.MindCare.model.ReplyLike;
import com.teamspring.MindCare.model.User;

public interface ReplyLikeRepository extends JpaRepository<ReplyLike, Long> {
    Optional<ReplyLike> findByReplyAndUser(Reply reply, User user);
}