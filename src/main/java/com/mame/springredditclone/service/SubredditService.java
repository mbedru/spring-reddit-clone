package com.mame.springredditclone.service;

import com.mame.springredditclone.dto.SubredditDto;
import com.mame.springredditclone.exceptions.SpringRedditException;
import com.mame.springredditclone.mapper.SubredditMapper;
import com.mame.springredditclone.model.Subreddit;
import com.mame.springredditclone.model.User;
import com.mame.springredditclone.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class SubredditService {

    private AuthService authService;
    private final SubredditRepository subredditRepository;
    private final SubredditMapper subredditMapper;
    @Transactional
    public SubredditDto save(SubredditDto subredditDto) {
        Subreddit save = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto, authService.getCurrentUser()));
        subredditDto.setSubredditId(save.getSubredditId());
        return subredditDto;
    }

    @Transactional(readOnly = true)
    public Collection<SubredditDto> getAll() {

/*        List<SubredditDto> collect = subredditRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(toList());
                log.info(collect.toString());*/
        return subredditRepository.findAll()
                .stream()
                .map(subredditMapper::mapSubredditToDto)
                .collect(toList());
    }

    public SubredditDto getSubreddit(Long id) {
         Subreddit subreddit = subredditRepository.findById(id).orElseThrow(()-> new SpringRedditException("No subreddit fount with the given ID."));
         return subredditMapper.mapSubredditToDto(subreddit);
    }
/* WE DON'T NEED BOTH B/C  __(((((MAPSTRUCT)))))__ WILL DO THE JOB OF MAPPING
    private Subreddit mapDtoToSubreddit(SubredditDto subredditDto) {//mapSubredditDto//mapSubredditDto mapping Dto to Subreddit
        return Subreddit.builder()
                .subredditName(subredditDto.getSubredditName())
                .description(subredditDto.getDescription())
                .build();
    }

    private SubredditDto mapToDto(Subreddit subreddit) {
        return SubredditDto.builder()
                .subredditId(subreddit.getSubredditId())
                .subredditName(subreddit.getSubredditName()) //.description(subreddit.getDescription())
                .numberOfPosts(subreddit.getPosts().size())
                .build();
    }*/
}
