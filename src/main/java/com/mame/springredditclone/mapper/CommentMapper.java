package com.mame.springredditclone.mapper;


import com.mame.springredditclone.dto.CommentDto;
import com.mame.springredditclone.model.Comment;
import com.mame.springredditclone.model.Post;
import com.mame.springredditclone.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;




@Mapper( componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "postId", expression = "java(comment.getPost().getPostId())")
    @Mapping(target = "userName", expression = "java(comment.getUser().getUsername())")
    CommentDto mapCommentToDto(Comment comment);

    @Mapping(target = "commentId", ignore = true)//why didnt we ignore the id's for subredditMapper and postMapper
    @Mapping(target = "text", source = "commentDto.text")
    @Mapping(target = "post", source = "post")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "user", source = "user")
    Comment mapDtoToComment(CommentDto commentDto, Post post, User user);
}
