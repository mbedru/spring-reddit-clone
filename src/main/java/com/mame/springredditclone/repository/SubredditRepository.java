package com.mame.springredditclone.repository;

import com.mame.springredditclone.model.Subreddit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubredditRepository extends JpaRepository<Subreddit, Long> {
    Optional<Subreddit> findBySubredditName(String subredditName);
}
