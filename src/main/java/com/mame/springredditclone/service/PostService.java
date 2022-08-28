package com.mame.springredditclone.service;

import com.mame.springredditclone.dto.PostRequest;
import com.mame.springredditclone.dto.PostResponse;
import com.mame.springredditclone.exceptions.PostNotFoundException;
import com.mame.springredditclone.exceptions.SubredditNotFoundException;
import com.mame.springredditclone.exceptions.SpringRedditException;
import com.mame.springredditclone.mapper.PostMapper;
import com.mame.springredditclone.model.Post;
import com.mame.springredditclone.model.Subreddit;
import com.mame.springredditclone.model.User;
import com.mame.springredditclone.repository.PostRepository;
import com.mame.springredditclone.repository.SubredditRepository;
import com.mame.springredditclone.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


import static java.util.stream.Collectors.*;

@Service @Slf4j
@AllArgsConstructor
public class PostService {
    private final SubredditRepository subredditRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PostMapper postMapper;

/*    @Transactional
    public void save(PostRequest postRequest) { //esu yeseraw be VOID return typw new.
        Subreddit subreddit = subredditRepository.findBySubredditName(postRequest.getSubredditName())
                    .orElseThrow(()-> new SubredditNotFoundException("SUBREDDIT :- "+postRequest.getSubredditName()+" not found!!!"));
        User currentUser = authService.getCurrentUser();

      postRepository.save(postMapper.mapRequestToPost(postRequest, subreddit, authService.getCurrentUser()));
    }*/
        @Transactional
    public PostResponse save(PostRequest postRequest) { //esu yeseraw be VOID return type new.
        Subreddit subreddit = subredditRepository.findBySubredditName(postRequest.getSubredditName())
                    .orElseThrow(()-> new SubredditNotFoundException("SUBREDDIT :- "+postRequest.getSubredditName()+" not found!!!"));
        User currentUser = authService.getCurrentUser();

        Post save = postRepository.save(postMapper.mapRequestToPost(postRequest, subreddit, currentUser));
        log.warn("********good :----"+ save.toString());
        PostResponse postResponse = postMapper.mapPostToResponse(save);
        log.warn("********good1 "+postResponse.toString() );
        return postResponse;
    }
    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(()-> new PostNotFoundException("sorry can not find a post with that ID :"+ id));
         return postMapper.mapPostToResponse(post);
    }
    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapPostToResponse)
                .collect(toList());
    }
    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubreddit (Long subredditId) {
        Subreddit subreddit = subredditRepository.findById(subredditId)
                .orElseThrow(()-> new SubredditNotFoundException("can't find subreddit with the specified id."));
        List<Post> post = postRepository.findAllBySubreddit(subreddit);
        return post.stream().map(postMapper::mapPostToResponse).collect(toList());
    }
    @Transactional
    public List<PostResponse> getAllByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("can't find USER with name :- "+username));
        List<Post> post = postRepository.findByUser(user);
        return post.stream().map(postMapper::mapPostToResponse).collect(toList());
    }





/*NOT USED B/C WE GOT PostMapper

    private Post mapDtoToPost(PostRequest postRequest) {
        return Post.builder()
                    .postId(postDto.getId()).build();
    }*/



}
