package com.mame.springredditclone.controller;

import com.mame.springredditclone.dto.AuthenticationResponse;
import com.mame.springredditclone.dto.LoginRequest;
import com.mame.springredditclone.dto.RefreshTokenRequest;
import com.mame.springredditclone.dto.RegisterRequest;
import com.mame.springredditclone.service.AuthService;
import com.mame.springredditclone.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController { ///port 25 for mailtrap might not work// i changed it to port:2525
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest){//RegisterRequest is a DTO
        authService.signup(registerRequest);
        return new ResponseEntity<>("User Registration Successful!!", OK);
    }
    @GetMapping("/accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token)
    {
        try {
            String verifiedUser = authService.verifyAccount(token);
            return new ResponseEntity<>("Congratulations, account activated successfully!! " + verifiedUser, OK);
        }catch (Exception e){
            return new ResponseEntity<>("Can't Activate Token : " + token +" :- "+ e.toString(), OK );//httpstatus .ok?? should be .error??
        }
    }
    @PostMapping("/login")
    public AuthenticationResponse Login (@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("refresh/token")
    public AuthenticationResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }
    @PostMapping("logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(OK).body("Refresh-Token Deleted Successfully!!");
    }
}
