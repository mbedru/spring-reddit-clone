package com.mame.springredditclone.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import java.time.Instant;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Subreddit {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long subredditId;
    @NotBlank(message = "Community/Subreddit name is required")
    @Column(unique = true)
    private String subredditName;
    @NotBlank(message = "Description is required")
    private String description;
    @OneToMany(fetch = LAZY)//we can add @JoinColumn(name="subredditId") but it's done automatically.
    private List<Post> posts;
    private Instant createdDate;
    @ManyToOne(fetch = LAZY)//we can add @JoinColumn(name="subredditId") but it's done automatically.
    private User user;
}
