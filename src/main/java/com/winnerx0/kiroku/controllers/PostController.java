package com.winnerx0.kiroku.controllers;

import com.winnerx0.kiroku.dto.PostDTO;
import com.winnerx0.kiroku.exceptions.NoDataFoundException;
import com.winnerx0.kiroku.models.Post;
import com.winnerx0.kiroku.services.PostService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/post")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService){
        this.postService = postService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Post>> getPosts() {
        return ResponseEntity.ok(postService.getPosts());
    }

    @PostMapping("/")
    public ResponseEntity<Post> createPost(@Valid @ModelAttribute PostDTO postDTO) throws IOException {
        return ResponseEntity.ok(postService.createPost(postDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePost(@PathVariable("id") String postId) throws IOException, NoDataFoundException {
        return ResponseEntity.ok(postService.deletePost(postId));
    }
}
