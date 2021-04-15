package com.example.adira.project.controller;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.adira.project.repository.loginRepository;
import com.example.adira.project.entity.loginEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

import java.util.Base64;
import java.util.Date;

@RestController
@RequestMapping(value = "/auth")

public class LoginController {

    @Autowired
    loginRepository login;

    @PostMapping(value = "login", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String login(loginEntity data ){
        loginEntity exist = login.login(data.username, data.password);
        String secretKey = Base64.getEncoder().encodeToString("WANGIWANGI".getBytes());
        System.out.println(secretKey);
        if(exist != null){
            Claims claims = Jwts.claims();
            claims.put("auth", data);
            Date now = new Date();
            Date valid = new Date(now.getTime() + 100000000);
            String Jwtdata = Jwts.builder().claim("id",exist.id).claim("username",exist.username).setIssuedAt(now).setExpiration(valid).signWith(SignatureAlgorithm.HS256, secretKey).compact();
            try {
                Jwts.parser().setSigningKey(secretKey).parse(Jwtdata);
                return "True | "+Jwtdata;
            }catch (JwtException e) {
                return "Invalid Jwt | "+Jwtdata;
            }
        }else{
            return "tidak ada user";
        }
    }

    @GetMapping(value = "decode/{jwt}")
    public Object decodeJwt (@PathVariable("jwt") String jwt){
        return  Jwts.parser().setSigningKey(Base64.getEncoder().encodeToString("WANGIWANGI".getBytes())).parse(jwt).getBody();
    }

}
