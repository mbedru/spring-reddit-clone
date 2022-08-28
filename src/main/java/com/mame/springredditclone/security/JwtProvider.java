package com.mame.springredditclone.security;

import com.mame.springredditclone.exceptions.SpringRedditException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.util.Date;

import static io.jsonwebtoken.Jwts.parserBuilder;
import static java.util.Date.from;

//turn parseClaimsJwt to parseClaimsJws OR it wont work.

/*@AllArgsConstructor
@RequiredArgsConstructor*//**/
@Service
public class JwtProvider {

    private KeyStore keyStore;

    @Value("${jwt.expiration.time}")
    private Long jwtExpirationInMillis;

    @PostConstruct //says @PostConstruct fn cant be final but still works, why??!!!???
    public void init() {
        try {
            keyStore = KeyStore.getInstance("JKS");//we are providing keystore of type jks
            InputStream resourceAsStream = getClass().getResourceAsStream("/spring-reddit.jks");//getting the input stream from the keystore file("springblog.jks)
            keyStore.load(resourceAsStream, "mame@123".toCharArray());//provide the input-stream && password-ofthe-keystore to the load method of the keystore
/*after that we read the privateKey from the keystore and pass it to the Jwts class to sign our JWT*/
        } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException e) {
            throw new SpringRedditException("Exception occured while loading keys" + "ERRors:>>>>>>> "+e.toString());
        }
    }

    public String generateToken (Authentication authentication){
        org.springframework.security.core.userdetails.User principal = (User)authentication.getPrincipal();//we have the user;/*now lets construct our JWT using Jwts class.*/
        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(from(Instant.now()))
                .signWith(getPrivateKey())
                .setExpiration(from(Instant.now().plusMillis(jwtExpirationInMillis)))
                .compact();//returns String
    }
//we can use  generateToken method in here(jwtProvider) but if the JWT is already expired then there'll be no user information inside the security context
// but b/c we need subject(username) as we generate token
    public String generateTokenWithUserName (String username){//subject(username) is from refreshTokenRequest
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(from(Instant.now()))
                .signWith(getPrivateKey())
                .setExpiration(from(Instant.now().plusMillis(jwtExpirationInMillis)))
                .compact();//returns String
    }

    private Key getPrivateKey() {
/*read the privateKey from the keystore and pass it to the Jwts class to sign our JWT*/
        try {
            return (PrivateKey) keyStore.getKey("spring-reddit", "mame@123".toCharArray());
        } catch ( UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e) {
            throw new SpringRedditException("Exception occured while retrieving public(prvt) key from keystore" + "ERRors:>>>>>>> "+e.toString());//getting private key lemalet new?????
        }
    }

    public boolean validateToken(String jwt) {
//        parser().setSigningKey(getPublicKey()); //since parser() use parserBuilder()
        parserBuilder().setSigningKey(getPublicKey())
                .build()
                .parseClaimsJws(jwt);//not sure if working/not?////jwt to jws
        return true;//means the jwt is validated
    }

    private PublicKey getPublicKey() {
        try {
            return keyStore.getCertificate("spring-reddit").getPublicKey();
        }catch (KeyStoreException e){
            throw new SpringRedditException("Exception occured while retrieving public key from keystore");
        }
    }
    public String getUsernameFromJwt(String token) {
        //get the claims (body of the token)// then =>> claims.getSubject blen username enagegnalen (because we passed username as subject)
        Claims claims = parserBuilder() //b/c parser() is deprecated
                .setSigningKey(getPublicKey())
                .build()                //b/c parser() is deprecated
                .parseClaimsJws(token)//jwt to jws
                .getBody();

        return claims.getSubject();
    }
    public Long getJwtExpirationInMillis(){
        return jwtExpirationInMillis;
    }
}
