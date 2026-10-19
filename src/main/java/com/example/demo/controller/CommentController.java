package com.example.demo.controller;

import com.example.demo.dto.common.ApiResponse;
import com.example.demo.dto.common.IdResponse;
import com.example.demo.dto.post.CommentDTO;
import com.example.demo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{newsfeed_id}")
    public ResponseEntity<List<CommentDTO>> getComment(@PathVariable("newsfeed_id") Long newsfeedId) {
        return ResponseEntity.ok(commentService.findAllByNewsfeed_Id(newsfeedId));
    }

    @PostMapping
    public ResponseEntity<IdResponse> addComment(@RequestBody CommentDTO commentDTO) {
        long id = commentService.addComment(commentDTO);
        return ResponseEntity.ok(new IdResponse(id));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @PutMapping
    public ResponseEntity<ApiResponse> updateComment(@RequestBody CommentDTO commentDTO) {
        commentService.updateComment(commentDTO);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
