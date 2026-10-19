package com.example.demo.service;

import com.example.demo.dto.post.PostDTO;
import com.example.demo.dto.post.UpdatePost;

import java.util.List;

public interface NewsfeedService {
    List<PostDTO> findAllByUserReceived(Long userId);
    long addPost(PostDTO postDTO);
    void addLikePost(UpdatePost updatePost);
    void dscLikePost(UpdatePost updatePost);
    void deletePost(int postId);
    List<PostDTO> findAllPostsByUserId(long userId);
    Long getUserId(long postId);
}
