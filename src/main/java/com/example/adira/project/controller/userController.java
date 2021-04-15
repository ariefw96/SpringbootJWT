package com.example.adira.project.controller;


import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.example.adira.project.entity.userEntity;
import com.example.adira.project.response.*;
import com.example.adira.project.service.userService;

import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping(value = "/user")

public class userController {

    private String secretKey = Base64.getEncoder().encodeToString("WANGIWANGI".getBytes());

    @Autowired
    responseGenerator response;


    @Autowired
    userService userService;

    public boolean checkJwt (String token){
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
            return response.failedResponse("token empty!", "Gagal!", 401);
        }else {
            token = token.split(" ")[1];
            if (checkJwt(token)) {
                userService.addNewUser(param);
                return response.successResponse(param, "Sukses menambahkan user baru.");
            } else {
                return response.failedResponse("JWT invalid", "Gagal!", 401);
            }
        }
    }

    @GetMapping(
            value = "getAllUser"
    )
    public responseData <List<userEntity>> getAllUser(@RequestHeader("token") String token){
        if (token.length() == 0){
            return response.failedResponse("token empty!", "Gagal!", 401);
        }else {
            token = token.split(" ")[1];
            if (checkJwt(token)) {
                List data = userService.getAllUser();
                return response.successResponse(data,"Sukses mendapatkan semua data");
            } else {
                return response.failedResponse("JWT invalid", "Gagal!", 401);
            }
        }
    }

    @GetMapping(value = "getUser/{id}")
    public responseData <userEntity> getUserById(@RequestHeader("token") String token, @PathVariable("id") Integer id){
        if (token.length() == 0){
            return response.failedResponse("token empty!", "Gagal!", 401);
        }else {
            token = token.split(" ")[1];
            if (checkJwt(token)) {
                userEntity data = userService.getUser(id);
                return response.successResponse(data, "Sukses mendapatkan data pada ID "+id);
            } else {
                return response.failedResponse("JWT invalid", "Gagal!", 401);
            }
        }

    }

    @PatchMapping(
            value = "updateUser/{id}",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
            )
    public responseData <String> updateUser(@RequestHeader("token") String token, @PathVariable("id") Integer id, userEntity param){
        if (token.length() == 0){
            return response.failedResponse("token empty!", "Gagal!", 401);
        }else {
            token = token.split(" ")[1];
            if (checkJwt(token)) {
                userService.updateUser(param,id);
                return response.successResponse(param,"Sukses mengubah data pada ID "+id);
            } else {
                return response.failedResponse("JWT invalid", "Gagal!", 401);
            }
        }
    }

    @DeleteMapping(value = "deleteUser/{id}")
    public responseData <String> deleteUser(@RequestHeader("token") String token, @PathVariable("id") Integer id){
        if (token.length() == 0){
            return response.failedResponse("token empty!", "Gagal!", 401);
        }else {
            token = token.split(" ")[1];
            if (checkJwt(token)) {
                userEntity data = userService.deleteUser(id);
                return response.successResponse(data, "Sukses menghapus user pada ID "+id);
            } else {
                return response.failedResponse("JWT invalid", "Gagal!", 401);
            }
        }
    }

}
