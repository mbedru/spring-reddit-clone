package com.mame.springredditclone.mapper;


import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.mame.springredditclone.dto.PostDto;
import com.mame.springredditclone.dto.PostRequest;
import com.mame.springredditclone.dto.PostResponse;
import com.mame.springredditclone.enumeration.VoteType;
import com.mame.springredditclone.model.Post;
import com.mame.springredditclone.model.Subreddit;
import com.mame.springredditclone.model.User;
import com.mame.springredditclone.model.Vote;
import com.mame.springredditclone.repository.CommentRepository;
import com.mame.springredditclone.repository.VoteRepository;
import com.mame.springredditclone.service.AuthService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Mapper(componentModel = "spring")//prepares it for dependency injection only when we need it; instead of creating instance of it automatically/while loading
public abstract class PostMapper {//changed interface to abstract class B/C we have added new fields in PostResponse/Dto we need some dependencies to fill these details in the mapper/here.

        @Autowired
        private CommentRepository commentRepository;
        @Autowired
        private VoteRepository voteRepository;
        @Autowired
        private AuthService authService;
        /*
        @AfterMapping
        protected void updateVoteCount(@MappingTarget Post post) {
        post.setVoteCount(0);
         }*/

    @Mapping(target = "createdDate" , expression = "java(java.time.Instant.now())")
    @Mapping(target = "subreddit", source = "subreddit")//target and source are same mention alemareg yichalal; gn esu atatfuachew beloal,,,b/c ndeparameter yalefe Object assign menadergew erasachen nen(not mapstruct)
    @Mapping(target = "user", source = "user")//target and source are same mention alemareg yichalal; gn esu atatfuachew beloal,,,b/c ndeparameter yalefe Object assign menadergew erasachen nen(not mapstruct)
    @Mapping(target = "description", source = "postRequest.description")
    //newly added
    @Mapping(target = "voteCount", constant = "0")//while saving new POST the voteCount of the post is 0.//b/c this mapper is called only in Post.create() function
    public abstract Post mapRequestToPost (PostRequest postRequest, Subreddit subreddit, User user);//map//OR mapDtoToPost//toCreatePostNotOnlyNeed PostRequest but also Subreddit & User


    @Mapping(target = "id", source = "postId")
//    @Mapping(target = "postName", source = "postName") //b/c target and source are same mention alemareg yichalal;
//    @Mapping(target = "description", source = "description")
//    @Mapping(target = "url", source = "url")
//    @Mapping(target = "voteCount", source = "voteCount")
    @Mapping(target = "subredditName", source = "subreddit.subredditName")
    @Mapping(target = "userName", source = "user.username")
    //newly added
    @Mapping(target = "commentCount", expression = "java(getCommentCount(post))")
    @Mapping(target = "duration", expression = "java(getDuration(post))")
    public abstract PostResponse mapPostToResponse(Post post);//OR mapPostToDto

    Integer getCommentCount(Post post) {
        return commentRepository.findByPost(post).size();
    }
    String getDuration(Post post) {
        return TimeAgo.using(post.getCreatedDate().toEpochMilli());
    }
/*

        @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
        @Mapping(target = "description", source = "postRequest.description")
        @Mapping(target = "subreddit", source = "subreddit")
        @Mapping(target = "voteCount", constant = "0")
        @Mapping(target = "user", source = "user")
        public abstract Post mapRequestToPost(PostRequest postRequest, Subreddit subreddit, User user);

        @Mapping(target = "id", source = "postId")
        @Mapping(target = "subredditName", source = "subreddit.subredditName")
        @Mapping(target = "userName", source = "user.username")
        @Mapping(target = "commentCount", expression = "java(getCommentCount(post))")
        @Mapping(target = "duration", expression = "java(getDuration(post))")
*/
/*        @Mapping(target = "upVote", expression = "java(isPostUpVoted(post))")
        @Mapping(target = "downVote", expression = "java(isPostDownVoted(post))")*//*

        public abstract PostResponse mapPostToResponse(Post post);
*/


/*
        boolean isPostUpVoted(Post post) {
            return checkVoteType(post, UPVOTE);
        }

        boolean isPostDownVoted(Post post) {
            return checkVoteType(post, DOWNVOTE);
        }
*/

/*        private boolean checkVoteType(Post post, VoteType voteType) {
            if (authService.isLoggedIn()) {
                Optional<Vote> voteForPostByUser =
                        voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post,
                                authService.getCurrentUser());
                return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType))
                        .isPresent();
            }
            return false;
        }*/

/*    private CommentRepository commentRepository;
    private VoteRepository voteRepository;
    private AuthService authService;//not used now

*/
}

