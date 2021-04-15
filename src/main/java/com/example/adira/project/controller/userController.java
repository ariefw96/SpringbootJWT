package com.example.adira.project.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.example.adira.project.entity.userEntity;
import com.example.adira.project.entity.jwtEntity;
import com.example.adira.project.response.*;
import com.example.adira.project.service.userService;

import java.util.List;

@RestController
@RequestMapping(value = "/user")

public class userController {

    @Autowired
    responseGenerator response;


    @Autowired
    userService userService;

    @Autowired
    jwtEntity jwt;

    @GetMapping(value = "")
    public responseData <String> Home(){
        return response.successResponse("API Loaded", "Sukses load API");
    }

    @PostMapping(
            value = "addNewUser",
        consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
    )
    public responseData <String> addNewUser(@RequestHeader("token") String token,userEntity param){
        if(jwt.returnJwt(token)){
            return response.successResponse(param, "Gagal menambahkan data!");
        }else {
            userService.addNewUser(param);
            return response.successResponse(param, "Sukses menambahkan user baru.");
        }
    }

    @GetMapping(
            value = "getAllUser"
    )
    public responseData <List<userEntity>> getAllUser(){
        List data = userService.getAllUser();
        return response.successResponse(data,"Sukses mendapatkan semua data");
    }

    @GetMapping(value = "getUser/{id}")
    public responseData <userEntity> getUserById(@PathVariable("id") Integer id){
        userEntity data = userService.getUser(id);
        return response.successResponse(data, "Sukses mendapatkan data pada ID "+id);
    }

    @PatchMapping(
            value = "updateUser/{id}",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
            )
    public responseData <String> updateUser(@PathVariable("id") Integer id, userEntity param){
        userService.updateUser(param,id);
        return response.successResponse(param,"Sukses mengubah data pada ID "+id);
    }

    @DeleteMapping(value = "deleteUser/{id}")
    public responseData <String> deleteUser(@PathVariable("id") Integer id){
        userEntity data = userService.deleteUser(id);
        return response.successResponse(data, "Sukses menghapus user pada ID "+id);
    }

}
