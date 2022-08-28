package com.mame.springredditclone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
    private Long postId;// //ke userToBackend simeta null; ke backendToUser simeta gn populate yideregal.
    private String subredditName;
    private String postName;
    private String url;
    private String description;
}
