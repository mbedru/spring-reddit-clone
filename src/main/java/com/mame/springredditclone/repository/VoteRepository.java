package com.mame.springredditclone.repository;

import com.mame.springredditclone.model.Post;
import com.mame.springredditclone.model.User;
import com.mame.springredditclone.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    //recent vote to a POST by a USER.
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
