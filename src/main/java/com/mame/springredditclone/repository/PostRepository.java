package com.mame.springredditclone.repository;

import com.mame.springredditclone.model.Post;
import com.mame.springredditclone.model.Subreddit;
import com.mame.springredditclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);
}
