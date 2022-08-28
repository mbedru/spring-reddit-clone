package com.mame.springredditclone.mapper;

import com.mame.springredditclone.dto.SubredditDto;
import com.mame.springredditclone.model.Post;
import com.mame.springredditclone.model.Subreddit;
import com.mame.springredditclone.model.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")  //X3//and for CDI, it would be "cdi"
public interface SubredditMapper {
	/*X1*///SubredditMapper INSTANCE = Mappers.getMapper( SubredditMapper.class);//ManualWayOfGettingInstance,//BetterWayIsToInjectTheMapperDirectlyWhereWeNeedIt((if our project uses any Dependency Injection solution))
//X2//MapStruct has solid support for both Spring and CDI(Contexts&Dependency-Injection)
	//so instead of line X1(manually-get-instance-of-mapper), we do X3(inject-the-mapper)

    @Mapping(target="numberOfPosts", expression = "java(mapPosts(subreddit.getPosts()))")
    SubredditDto mapSubredditToDto(Subreddit subreddit);


    default Integer mapPosts(List<Post> posts) {//returns number of posts from list of posts(from subreddit)
        return posts.size();
    }

    @InheritInverseConfiguration//still works without this annotation.
    @Mapping(target = "posts", ignore = true)
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "user", source = "user")
    Subreddit mapDtoToSubreddit(SubredditDto subredditDto, User user);
	/*
	 * @InheritInverseConfiguration
	 * 
	 * @Mapping(target = "posts", ignore = true) Subreddit
	 * mapDtoToSubreddit(SubredditDto subreddit);
	 */
}




/*@Mapper(componentModel = "spring")
public interface SubredditMapper {

    @Mapping(target = "numberOfPosts", expression = "java(mapPosts(subreddit.getPosts()))")
    SubredditDto mapSubredditToDto(Subreddit subreddit);

    default Integer mapPosts(List<Post> numberOfPosts) {
        return numberOfPosts.size();
    }

    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    Subreddit mapDtoToSubreddit(SubredditDto subreddit);
}*/
