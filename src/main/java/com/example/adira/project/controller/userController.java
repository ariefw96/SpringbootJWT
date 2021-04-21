package com.example.adira.project.controller;


import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.example.adira.project.entity.userEntity;
import com.example.adira.project.response.*;
import com.example.adira.project.service.userService;
import com.example.adira.project.exception.*;

import org.springframework.core.env.Environment;

import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping(value = "api/v1/user")

public class userController {

    @Autowired
    private Environment env;

//    protected String secretKey = Base64.getEncoder().encodeToString(env.getProperty("secret.key").getBytes());

    @Autowired
    responseGenerator response;


    @Autowired
    userService userService;

    public boolean checkJwt (String token){
        String secretKey = Base64.getEncoder().encodeToString(env.getProperty("secret.key").getBytes());
        try{
            Jwts.parser().setSigningKey(secretKey).parse(token);
            return true;
        }catch (JwtException e){
            return false;
        }
    }

    @GetMapping(value = "")
    public responseData <String> Home(){
        return response.successResponse("API Loaded", "Sukses load API");
    }

    @PostMapping(
            value = "addNewUser",
        consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
    )
    public responseData <String> addNewUser(@RequestHeader("token") String token,userEntity param){
        if (token.length() == 0){
            throw new unauthorizedException("Unauthorized Access!",401);
        }else {
            token = token.split(" ")[1];
            if (checkJwt(token)) {
                userService.addNewUser(param);
                return response.successResponse(param, "Sukses menambahkan user baru.");
            } else {
                throw new unauthorizedException("Unauthorized Access!",401);
            }
        }
    }

    @GetMapping(
            value = "getAllUser"
    )
    public ResponseEntity <responseData<List<userEntity>>> getAllUser(){
        try{
            List data = userService.getAllUser();
            if(data.size() < 1){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response.successResponse(data,"Data tidak ditemukan"));
            }else {
//        return new ResponseEntity<List<userEntity>>(data, HttpStatus.OK);
                return ResponseEntity.status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response.successResponse(data, "Sukses menampilkan data"));
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response.successResponse(e,"INTERNAL SERVER ERROR"));
        }
    }

    @GetMapping(value = "getUser/{id}")
    public responseData <userEntity> getUserById(@PathVariable("id") Integer id){
        userEntity data = userService.getUser(id);
        System.out.println(data);
        return response.successResponse(data, "Sukses mendapatkan data pada ID "+id);
    }

    @PatchMapping(
            value = "updateUser/{id}",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
            )
    public responseData <String> updateUser(@RequestHeader("token") String token, @PathVariable("id") Integer id, userEntity param){
        if (token.length() == 0){
            throw new unauthorizedException("Unauthorized Access!",401);
        }else {
            token = token.split(" ")[1];
            if (checkJwt(token)) {
                userService.updateUser(param,id);
                return response.successResponse(param,"Sukses mengubah data pada ID "+id);
            } else {
                throw new unauthorizedException("Unauthorized Access!",401);
            }
        }
    }

    @DeleteMapping(value = "deleteUser/{id}")
    public responseData <String> deleteUser(@RequestHeader("token") String token, @PathVariable("id") Integer id){
        if (token.length() == 0){
            throw new unauthorizedException("Unauthorized Access!",401);
        }else {
            token = token.split(" ")[1];
            if (checkJwt(token)) {
                userEntity data = userService.deleteUser(id);
                return response.successResponse(data, "Sukses menghapus user pada ID "+id);
            } else {
                throw new unauthorizedException("Unauthorized Access!",401);
            }
        }
    }



}
