package com.mame.springredditclone.service;

import com.mame.springredditclone.dto.CommentDto;
import com.mame.springredditclone.exceptions.PostNotFoundException;
import com.mame.springredditclone.mapper.CommentMapper;
import com.mame.springredditclone.model.Comment;
import com.mame.springredditclone.model.NotificationEmail;
import com.mame.springredditclone.model.Post;
import com.mame.springredditclone.model.User;
import com.mame.springredditclone.repository.CommentRepository;
import com.mame.springredditclone.repository.PostRepository;
import com.mame.springredditclone.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.*;

@Service
@AllArgsConstructor
public class CommentService {
    //TODO: Construct POST URL
    private static final String POST_URL = "";//not implemented
    private CommentRepository commentRepository;
    private UserRepository userRepository;//used if we have findByUser
    private PostRepository postRepository;
    private AuthService authService;
    private CommentMapper commentMapper;
    private MailContentBuilder mailContentBuilder;//1.build the email
    private MailService mailService;//2.then send the email that we build

    @Transactional
    public CommentDto save(CommentDto commentDto) {
        Post post = postRepository.findById(commentDto.getPostId())
                .orElseThrow(()-> new PostNotFoundException("can't find post with id : "+ commentDto.getPostId().toString()));
        User commenter = authService.getCurrentUser();
        Comment comment = commentMapper.mapDtoToComment(commentDto, post, commenter);
        Comment save = commentRepository.save(comment);

        String message = mailContentBuilder.build(commenter.getUsername() + " posted a comment on your post. " + POST_URL);//mailContentBuilder.build will return message in html format//then we store as String.
        sendCommentNotification(commenter,message, post.getUser());

        return commentMapper.mapCommentToDto(comment);
    }
    @Transactional(readOnly = true)
    public List<CommentDto> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new PostNotFoundException("cant find post with id : "+ postId));
        return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapCommentToDto)
                .collect(toList());
    }
    @Transactional(readOnly = true)
    public List<CommentDto> getAllCommentsForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("cant find user : "+ username));

        return commentRepository.findByUser(user)
                .stream()
                .map(commentMapper::mapCommentToDto)
                .collect(toList());
    }

    private void sendCommentNotification (User commenter, String message, User postOwner) {
        mailService.sendMail(new NotificationEmail(commenter.getUsername()+" commented(CommServ) on your post", postOwner.getEmail(), message));
    }
}
