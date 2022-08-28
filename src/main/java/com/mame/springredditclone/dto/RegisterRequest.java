package com.mame.springredditclone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//this is like the template of how data is transfered-to-controller while registering Register
public class RegisterRequest {//dto/data-Transfer-Object
    private String email;
    private String username;
    private String password;
}
