package com.teamspring.MindCare.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teamspring.MindCare.model.Post;
import com.teamspring.MindCare.model.PostLike;
import com.teamspring.MindCare.model.UserTemp;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByPostAndUser(Post post, UserTemp user);
}