package com.example.demo.service;

import com.example.demo.dto.post.CommentDTO;

import java.util.List;

public interface CommentService {
    List<CommentDTO> findAllByNewsfeed_Id(Long newsfeedId);
    long addComment(CommentDTO comment);
    void deleteComment(long commentId);
    void updateComment(CommentDTO comment);
}
