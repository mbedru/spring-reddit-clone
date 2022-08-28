package com.mame.springredditclone.controller;

import com.mame.springredditclone.dto.SubredditDto;
import com.mame.springredditclone.service.SubredditService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/subreddit")
@AllArgsConstructor//can be @RequiredArgsConstructor
@Slf4j
public class SubredditController {
    private final SubredditService subredditService;

    @PostMapping//we are not having @RequestMapping but spring:- POST request simeta automatically kezi method ga yagenagnewal yihe bcha slwhone the only post-mapping
    public ResponseEntity<SubredditDto> createSubreddit(@RequestBody SubredditDto subredditDto) {
        log.warn("--------**********createSubreddit called ********--------"+subredditDto.toString());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subredditService.save(subredditDto));
    }
    @GetMapping//spring:- GET request simeta automatically kezi method ga yagenagnewal yihe bcha slwhone the only get-mapping
    public ResponseEntity<Collection<SubredditDto>> getAllSubreddits() {
        log.warn("--------**********getAllSubreddits called ********--------");
        return ResponseEntity.status(HttpStatus.OK)
                .body(subredditService.getAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<SubredditDto> getSubreddit(@PathVariable Long id) {
        log.warn("--------**********getSubreddit called ********--------");
        return ResponseEntity.status(HttpStatus.OK)
                .body(subredditService.getSubreddit(id));
    }
}
