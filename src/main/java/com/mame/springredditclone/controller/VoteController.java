package com.mame.springredditclone.controller;

import com.mame.springredditclone.dto.VoteDto;
import com.mame.springredditclone.service.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vote")
@AllArgsConstructor
public class VoteController {

    private final VoteService voteService;
    @PostMapping
    public ResponseEntity<VoteDto> vote(@RequestBody VoteDto voteDto) {
       return ResponseEntity.status(HttpStatus.CREATED)
               .body(voteService.vote(voteDto));
    }

}
