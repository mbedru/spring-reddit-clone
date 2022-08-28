package com.mame.springredditclone.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import java.time.Instant;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long postId;//
    @NotBlank(message = "Post Name cannot be empty or Null")
    private String postName;//
    @Nullable
    private String url;//
    @Nullable
    @Lob
    private String description;//
    private Integer voteCount = 0;//
    @ManyToOne(fetch = LAZY)//many Post to one User with id "userId"
    @JoinColumn(name = "userId", referencedColumnName = "userId" )
    private User user;//
    private Instant createdDate;//
    @ManyToOne(fetch = LAZY)//many Post to one Subreddit with id "subredditId"
    @JoinColumn(name = "subredditId", referencedColumnName = "subredditId")//both"id" are subredditId
    private Subreddit subreddit;
}

