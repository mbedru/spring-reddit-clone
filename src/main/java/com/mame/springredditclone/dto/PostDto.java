package com.mame.springredditclone.dto;

import com.mame.springredditclone.model.Subreddit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
//************))))))))88888we are not using this dto so far88888))))))))&&&&&&*****
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {
    private Long id;
    private Subreddit subreddit;
}
