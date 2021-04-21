package com.example.adira.project.controller;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.adira.project.response.*;
import com.example.adira.project.repository.loginRepository;
import com.example.adira.project.entity.loginEntity;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.env.Environment;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

import com.example.adira.project.exception.*;

import java.util.Base64;
import java.util.Date;

@RestController
@RequestMapping(value = "api/v1/auth")

public class LoginController {

    @Autowired
    private  Environment env;

//    protected String secretKey = Base64.getEncoder().encodeToString(env.getProperty("secret.key").getBytes());

    @Autowired
    loginRepository login;

    @Autowired
    responseGenerator response;

    @PostMapping(value = "login", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public responseData<String> login(loginEntity data ) throws notfoundException {
        String secretKey = Base64.getEncoder().encodeToString(env.getProperty("secret.key").getBytes());
        loginEntity exist = login.login(data.username, data.password);
        if(exist != null){
            Date now = new Date();
            Date valid = new Date(now.getTime() + 3600000); //valid for 1 hour
            String Jwtdata = Jwts.builder().setHeaderParam("typ","JWT")
                    .claim("id",exist.id).claim("username",exist.username).claim("isLogin", true).setIssuedAt(now).setExpiration(valid).signWith(SignatureAlgorithm.HS256, secretKey).compact();
            return  response.successResponse(Jwtdata,"Sukses Login.");
        }else{
            throw new notfoundException(404, "404 Not Found");
        }
    }

    @GetMapping(value = "decode/{jwt}")
    public Object decodeJwt (@PathVariable("jwt") String jwt){
        return  Jwts.parser().setSigningKey(Base64.getEncoder().encodeToString(env.getProperty("secret.key").getBytes())).parse(jwt).getBody();
    }

}
