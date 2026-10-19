package com.example.demo.controller;

import com.example.demo.dto.common.ApiResponse;
import com.example.demo.dto.common.IdResponse;
import com.example.demo.dto.post.PostDTO;
import com.example.demo.dto.post.PostImageRequest;
import com.example.demo.dto.post.UpdatePost;
import com.example.demo.service.ImageService;
import com.example.demo.service.NewsfeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/newsfeed")
@RequiredArgsConstructor
public class NewsfeedController {

    private final NewsfeedService newsfeedService;
    private final ImageService imageService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<PostDTO>> getNewsfeed(@PathVariable Long userId) {
        return ResponseEntity.ok(newsfeedService.findAllByUserReceived(userId));
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<List<PostDTO>> getProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(newsfeedService.findAllPostsByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<IdResponse> saveNewsfeed(@RequestBody PostDTO postDTO) {
        long id = newsfeedService.addPost(postDTO);
        return ResponseEntity.ok(new IdResponse(id));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse> deleteNewsfeed(@PathVariable int postId) {
        newsfeedService.deletePost(postId);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @PutMapping("/addlike")
    public ResponseEntity<ApiResponse> addLike(@RequestBody UpdatePost updatePost) {
        newsfeedService.addLikePost(updatePost);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @PutMapping("/removelike")
    public ResponseEntity<ApiResponse> dscLike(@RequestBody UpdatePost updatePost) {
        newsfeedService.dscLikePost(updatePost);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @PostMapping("/image")
    public ResponseEntity<ApiResponse> uploadImage(@RequestBody PostImageRequest postImageRequest) {
        imageService.save(postImageRequest);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
