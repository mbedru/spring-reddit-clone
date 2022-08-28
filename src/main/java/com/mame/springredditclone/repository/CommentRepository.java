package com.mame.springredditclone.repository;

import com.mame.springredditclone.model.Comment;
import com.mame.springredditclone.model.Post;
import com.mame.springredditclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);
    List<Comment> findByUser(User user);
}
