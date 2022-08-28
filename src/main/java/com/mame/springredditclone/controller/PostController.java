package com.mame.springredditclone.controller;


import com.mame.springredditclone.dto.PostRequest;
import com.mame.springredditclone.dto.PostResponse;
import com.mame.springredditclone.service.PostService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post")
@AllArgsConstructor @Slf4j
public class PostController {

    private final PostService postService;

/*    @PostMapping()
    public ResponseEntity<Void> createPost (@RequestBody PostRequest postRequest) {
        postService.save(postRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }*/
        @PostMapping()
    public ResponseEntity<PostResponse> createPost (@RequestBody PostRequest postRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postService.save(postRequest));
    }
    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts () {
        return ResponseEntity.status(HttpStatus.OK)
                .body(postService.getAllPosts());
    }
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(postService.getPost(id));
    }
    @GetMapping("by-subreddit/{id}")
    public ResponseEntity<List<PostResponse>> getPostsBySubreddit(@PathVariable Long subreddidId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(postService.getPostsBySubreddit(subreddidId));
    }
    @GetMapping("by-user/{username}")
    public ResponseEntity<List<PostResponse>> getPostsByUser (@PathVariable String username) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(postService.getAllByUser(username));
    }
}
