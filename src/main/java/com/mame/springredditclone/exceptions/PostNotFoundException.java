package com.mame.springredditclone.exceptions;

public class PostNotFoundException extends RuntimeException{
    public PostNotFoundException(String exMessage) {
        super(exMessage);
    }
}
