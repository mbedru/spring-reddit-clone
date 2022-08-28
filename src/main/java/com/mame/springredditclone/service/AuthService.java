package com.mame.springredditclone.service;

import com.mame.springredditclone.dto.AuthenticationResponse;
import com.mame.springredditclone.dto.LoginRequest;
import com.mame.springredditclone.dto.RefreshTokenRequest;
import com.mame.springredditclone.dto.RegisterRequest;
import com.mame.springredditclone.exceptions.SpringRedditException;
import com.mame.springredditclone.model.NotificationEmail;
import com.mame.springredditclone.model.User;
import com.mame.springredditclone.model.VerificationToken;
import com.mame.springredditclone.repository.UserRepository;
import com.mame.springredditclone.repository.VerificationTokenRepository;
import com.mame.springredditclone.security.JwtProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class AuthService {//contains main business logic to register the users(1.creatingUserObject 2.saving to the DB 3.sending out activation email etc... )
    /****__________ * FOR SIGNING-UP & SENDING EMAIL TO PROVE THE USER IS THEM (VERIFICATION) THEN ENABLING THEIR ACC. FOR USAGE AFTER THEY SEND THE TOKEN/LINK IN THEIR EMAIL* ________******/
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    @Transactional
    public void signup(RegisterRequest registerRequest){
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreatedDate(Instant.now());
        user.setEnabled(false);//TRUE after user is verified through email token

        userRepository.save(user);// saving user to DB
        //sending account activation Email to the user(Generating verification token after saving user-to-DB & send that token as part of the Verification-Email)
        String token = generateVerificationToken(user);
        //sending activation token to User as Email.
        //we need templateEngine called tymeleaf to create templete(w/h we will use to send emails)
        String message = mailContentBuilder.build("Thank you "+user.getUsername()+" for  signing up to Spring-Reddit, "
                + "Please click on the below URL to activate your account : "
                + "http://localhost:8080/api/auth/accountVerification/" + token);//mailContentBuilder.build will return message in html format//then we store as String//then pass to mail sender
        mailService.sendMail(new NotificationEmail("Please activate your account!!"
                ,user.getEmail(), message));
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
         String principal = (String) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
            return userRepository.findByUsername(principal)
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal));
        /*  org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));*/
    }

    private String  generateVerificationToken (User user){
        String token = UUID.randomUUID().toString();//unique,random value used as our verification token//af ter sending the email to user, eske 2,3 days sewyew may not click the link/authenticate, all this time demo in memory laykemet yichelal//soo save/persist the The token in DB & when the user verifies the email we will search the token in DB and enable the user.
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);//why arent we saving expiry date too?in the verificationToken
        verificationTokenRepository.save(verificationToken);
        return token;//we are just sending the token-String to the user; kelay yalew is saving the verification token(containing user,expiryDate,theToken)
                //so that behuala sewyew verify siyareg ke db awteten enable enaregewalen
    }

    public String verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);//optional kaln orElseThrow malet ayitebedbnm and vice-versa
        String verifiedUser = fetchAndEnableUser(verificationToken.get());//when the repository returns <optional> thing, the receiving object also be <optional>// also we must add .get() when accessing/writingOn the obj
        return verifiedUser;
    }
    @Transactional
    private String fetchAndEnableUser(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new SpringRedditException("User not found with name: " + username));
        user.setEnabled(true);
        userRepository.save(user);
        return user.getUsername();
    }
/****_____* AUTHENTICATION & AUTHORIZATION USING JWT *_____****/
    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);//store the Auth-Obj inside the SecurityContext///to-check if-user-logged-in,just check securityContext for-the Auth-Obj if u find Auth-Obj then logged in;
        String token = jwtProvider.generateToken(authenticate);
//        return new AuthenticationResponse(token, loginRequest.getUsername());
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(loginRequest.getUsername())
                .build();
    }

    // the refresh Token function is BAD it will accept whatever string u throw at it as a username.
    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {//will create, delete, and validate refresh tokens
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());//if valid then generate new token
        String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(refreshTokenRequest.getUsername())
                .build();
    }

}

/*
//these were inside
   public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(refreshTokenRequest.getUsername())
                .build();
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }*/
