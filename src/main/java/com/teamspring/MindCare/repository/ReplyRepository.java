package com.teamspring.MindCare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teamspring.MindCare.model.Reply;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {
}