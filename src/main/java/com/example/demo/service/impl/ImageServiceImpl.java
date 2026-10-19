package com.example.demo.service.impl;

import com.example.demo.dto.post.PostImageRequest;
import com.example.demo.model.Newsfeed;
import com.example.demo.model.Post_Image;
import com.example.demo.repository.ImageRepository;
import com.example.demo.repository.NewsfeedRepository;
import com.example.demo.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private final NewsfeedRepository newsfeedRepository;

    @Override
    public void save(PostImageRequest postImageRequest) {
        Newsfeed newsfeed = newsfeedRepository.findById(postImageRequest.getPostId());
        Post_Image post_image = new Post_Image();
        post_image.setNewsfeed(newsfeed);
        post_image.setImgUrl(postImageRequest.getImageUrl());
        imageRepository.save(post_image);
    }
}
