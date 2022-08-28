package com.mame.springredditclone.exceptions;

public class SubredditNotFoundException extends RuntimeException{
    public SubredditNotFoundException(String exMessage) {
        super(exMessage);
    }
}
