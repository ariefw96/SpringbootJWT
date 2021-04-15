package com.example.adira.project.controller;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.adira.project.response.*;
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

    private String secretKey = Base64.getEncoder().encodeToString("WANGIWANGI".getBytes());

    @Autowired
    loginRepository login;

    @Autowired
    responseGenerator response;

    @PostMapping(value = "login", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public responseData<String> login(loginEntity data ){
        loginEntity exist = login.login(data.username, data.password);
        if(exist != null){
            Date now = new Date();
            Date valid = new Date(now.getTime() + 100000000);
            String Jwtdata = Jwts.builder().claim("id",exist.id).claim("username",exist.username).setIssuedAt(now).setExpiration(valid).signWith(SignatureAlgorithm.HS256, secretKey).compact();
            return  response.successResponse(Jwtdata,"Sukses Login.");
        }else{
            return response.successResponse("Username atau password tidak ditemukan","Gagal Login!");
        }
    }

    @GetMapping(value = "decode/{jwt}")
    public Object decodeJwt (@PathVariable("jwt") String jwt){
        return  Jwts.parser().setSigningKey(Base64.getEncoder().encodeToString("WANGIWANGI".getBytes())).parse(jwt).getBody();
    }

}
