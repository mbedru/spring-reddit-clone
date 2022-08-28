package com.mame.springredditclone.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubredditDto {//create lemareg only name & desc miyasfelgut; but read lemareg(id&noofpost) we add id&noofpost too.
    private Long subredditId;
    private String subredditName;
    private String description;
    private Integer numberOfPosts;
}
