package com.mame.springredditclone.service;

import com.mame.springredditclone.dto.VoteDto;
import com.mame.springredditclone.exceptions.PostNotFoundException;
import com.mame.springredditclone.exceptions.SpringRedditException;
import com.mame.springredditclone.model.Post;
import com.mame.springredditclone.model.Vote;
import com.mame.springredditclone.repository.CommentRepository;
import com.mame.springredditclone.repository.PostRepository;
import com.mame.springredditclone.repository.VoteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.mame.springredditclone.enumeration.VoteType.DOWNVOTE;
import static com.mame.springredditclone.enumeration.VoteType.UPVOTE;

@Service
@AllArgsConstructor @Slf4j
public class VoteService {
    private VoteRepository voteRepository;
    private PostRepository postRepository;
    private CommentRepository commentRepository;
    private AuthService authService;
    private MailContentBuilder mailContentBuilder;//1.build the email
    private MailService mailService;//2.then send the email that we build

    @Transactional
    public VoteDto vote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("can't find post with id : "+voteDto.getPostId()));
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());//the latest(TOP)by the user on that post
        log.warn("----"+post.toString()+"-----"+voteByPostAndUser.toString());
        if (voteByPostAndUser.isPresent() &&
                voteByPostAndUser.get().getVoteType()
                        .equals(voteDto.getVoteType())) {
            throw new SpringRedditException("You have already "+ voteDto.getVoteType()+ "'d this post");
        }
        if (UPVOTE.equals(voteDto.getVoteType())) {
            post.setVoteCount(post.getVoteCount() +1);
        }
        else { //if DOWNVOTE
            post.setVoteCount(post.getVoteCount() -1);
        }
        voteRepository.save(mapToVote(voteDto, post));
        postRepository.save(post);//b/c we changed voteCount property inside post.
    return voteDto; //returning the same dto if the Vote is successful.
    }

    private Vote mapToVote(VoteDto voteDto, Post post) { // we dont want to create Mapper for such small model
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}
