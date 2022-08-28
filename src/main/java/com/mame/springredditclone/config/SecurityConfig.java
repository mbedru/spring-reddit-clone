package com.mame.springredditclone.config;

import com.mame.springredditclone.security.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@AllArgsConstructor
@EnableWebSecurity//enables the springsecurity module (that we added in the dependency)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Override
    public void configure (HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()//disable csrf-protection for our backend//csrf attacks mainly occur when there are sessions & we using cookies to auth the session info.//as REST-api's are stateless(no session/cookies just token-based) by definition, & as we are using JWT for auth we can disable the csrf-protection feature.
                .authorizeRequests()
                .antMatchers("/api/auth/**").permitAll() //allow them if their url starts with "/api/auth/**"
                .antMatchers(HttpMethod.GET,"/api/subreddit").permitAll()
                .anyRequest().authenticated(); //keza wechi path yalachewn authenticate argachew(auth sayalfu wede backend malef aychelum)
        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);//tell spring to check for the access/jwt token before trying the UsernamePasswordAuthenticationFilter scheme.
    }

    //we can add @Autowired for dependency-injection but we already have @AllargsConstructor
    public void configGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }
    @Bean(BeanIds.AUTHENTICATION_MANAGER )
    PasswordEncoder passwordEncoder() { //as the PasswordEncoder is an interface, we have to create-a-bean manually here SecurityConfig //& whenever we autowire this bean we get instance of BcryPaEncoder
        return new BCryptPasswordEncoder();
    }
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
