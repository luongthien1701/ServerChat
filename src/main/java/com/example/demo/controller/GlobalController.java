package com.example.demo.controller;

import com.example.demo.dto.search.SelectSearch;
import com.example.demo.dto.upload.UploadResponse;
import com.example.demo.service.SearchService;
import com.example.demo.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GlobalController {

    private final SearchService searchService;
    private final UploadService uploadService;

    @GetMapping("/search/{userId}/{like}")
    public ResponseEntity<List<SelectSearch>> findAllByLikeDisplayName(
            @PathVariable Long userId,
            @PathVariable String like
    ) {
        return ResponseEntity.ok(searchService.searchAll(userId, like));
    }

    @PostMapping("/upload")
    public ResponseEntity<UploadResponse> uploadFile(
            @RequestParam("image") MultipartFile img,
            @RequestParam("type") String type,
            @RequestParam("ownerId") Long ownerId
    ) throws Exception {
        String url = uploadService.save(img, type, ownerId);
        if (url != null) {
            return ResponseEntity.ok(new UploadResponse(url));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
