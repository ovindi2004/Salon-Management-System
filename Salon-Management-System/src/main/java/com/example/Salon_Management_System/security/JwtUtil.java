package com.example.Salon_Management_System.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component //Spring Boot එක මේ class එක Bean එකක් විදිහට manage කරනවා.
public class JwtUtil {

    @Value("${jwt.secret}")  //මේ values එන්නේ application.properties එකෙන්.
    private String secretKey;

    @Value("${jwt.expiration}") //මේ values එන්නේ application.properties එකෙන්.
    private Long expiration;



    public String generateToken(com.example.Salon_Management_System.dto.UserDTO userDTO) { //Login success උනාම JWT token එක හදන method එක.

        Map<String, Object> claims = new HashMap<>(); //JWT එකේ extra data store කරන place එක.

        claims.put("userId", userDTO.getUserId());
        claims.put("userName", userDTO.getUserName());
        claims.put("userRole", userDTO.getRole());



        return Jwts.builder() //JWT token එක create කරනවා.
                .claims(claims) //අපි කලින් හදාගත්ත user data token එකට add කරනවා.
                .subject(userDTO.getUserName()) //JWT standard field එක. මෙතන username එක දානවා.
                .issuedAt(new Date())//Token create වුණ වෙලාව.
                .expiration(new Date(System.currentTimeMillis() + expiration))//Token expire වෙන time එක.
                .signWith(getSecretKey())//Secret key එකෙන් token එක encrypt/sign කරනවා. ඒකෙන් token එක වෙනස් කරලා fake කරන්න බැහැ.
                .compact();
    }


    public String extractUsername(String token) {//Token එක ඇතුලේ තියෙන username ගන්න.

        return extractAllClaims(token).getSubject();
    }


    public Date extractExpiration(String token) { //Token expire වෙන date එක return කරනවා.

        return extractAllClaims(token).getExpiration();
    }


    private Claims extractAllClaims(String token) { //මේක තමයි main method එක.Token එක parse කරලා data ටික ගන්නවා.

        return Jwts.parser()
                .verifyWith(getSecretKey())//sign කරද්දි පාවිච්චි කරන key එකම verify කරද්දිත් පාවිච්චි කරනවා.
                .build()
                .parseSignedClaims(token)//JWT එක decode කරනවා.
                .getPayload();//Data ටික ගන්නවා.
    }


    private SecretKey getSecretKey() { //String secret එක byte array එකකට convert කරනවා.

        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);

        return Keys.hmacShaKeyFor(keyBytes);//JWT signing key එක හදනවා.
    }


    public boolean isTokenExpired(String token) { //Token Expire වෙලාද බලන එක

        return extractExpiration(token)
                .before(new Date());
    }


    public Boolean validateToken(String token, UserDetails userDetails) { //token එක valid ද බලනවා.

        String username = extractUsername(token); //Username ගන්නවා

        return username.equals(userDetails.getUsername()) //Compare කරනවා:Token එකේ username
                && !isTokenExpired(token); //Expire නැද්ද බලනවා:
    }
}