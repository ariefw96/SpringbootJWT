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


    public boolean checkJwt(String token) {
        String secretKey = Base64.getEncoder().encodeToString(env.getProperty("secret.key").getBytes());
        try {
            Jwts.parser().setSigningKey(secretKey).parse(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    @GetMapping(value = "")
    public responseData<String> Home() {
        return response.successResponse("API Loaded", "Sukses load API");
    }

    @PostMapping(
            value = "addNewUser",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
    )
    public ResponseEntity<responseData<String>> addNewUser(@RequestHeader("token") String token, userEntity param) {
        if (token.length() == 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response.failedResponse("TOKEN EMPTY", "Parse Jwt Failed", 401));
        } else {
            try {
                token = token.split(" ")[1];
                if (checkJwt(token)) {
                    try {
                        userService.addNewUser(param);
                        return ResponseEntity.status(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(response.successResponse(param, "Berhasil menambahkan user baru"));
                    } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(response.failedResponse(e, "INTERNAL SERVER ERROR", 500));
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(response.failedResponse("Either JWT expired or Invalid.", "Parse Jwt Failed", 401));
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response.failedResponse(e.getLocalizedMessage(), "Parse Jwt Failed", 401));
            }
        }
    }

    @GetMapping(
            value = "getAllUser"
    )
    public ResponseEntity<responseData<List<userEntity>>> getAllUser() {
        try {
            List data = userService.getAllUser();
            if (data.size() < 1) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response.successResponse(data, "Data tidak ditemukan"));
            } else {
                return ResponseEntity.status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response.successResponse(data, "Sukses menampilkan data"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response.successResponse(e.getLocalizedMessage(), "INTERNAL SERVER ERROR"));
        }
    }


    @GetMapping(value = "getUser/{id}")
    public ResponseEntity<responseData<userEntity>> getUserById(@PathVariable("id") Integer id) {
        if(id == 0){
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response.failedResponse("EMPTY ID","METHOD NOT ALLOWED",403));
        }
        try{
            userEntity data = userService.getUser(id);
            if (data == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response.failedResponse(null,"Data tidak ditemukan", 404));
            }else{
                return ResponseEntity.status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response.successResponse(data,"Berhasil mendapatkan data"));
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response.failedResponse(e,"INTERNAL SERVER ERROR",500));
        }
    }

    @PatchMapping(
            value = "updateUser/{id}",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
    )
    public ResponseEntity<responseData<String>> updateUser(@RequestHeader("token") String token, @PathVariable("id") Integer
            id, userEntity param) {
        if(userService.getUser(id) == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response.failedResponse("ID "+id+" CAN'T BE FOUND ON DATABASE", "404 NOT FOUND", 404));
        }
        if (token.length() == 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response.failedResponse("TOKEN EMPTY", "Parse Jwt Failed", 401));
        } else {
            try {
                token = token.split(" ")[1];
                if (checkJwt(token)) {
                    try {
                            //do something here || 200
                            userService.updateUser(param,id);
                            return ResponseEntity.status(HttpStatus.OK)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .body(response.successResponse(param, "Sukses update data pada ID : "+id));
                    } catch (Exception e) {
                            //catch error || 500
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(response.failedResponse(e,"INTERNAL SERVER ERROR",500));
                    }
                }else{
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(response.failedResponse("Either JWT expired or Invalid.", "Parse Jwt Failed", 401));
                }
            }catch (Exception e){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response.failedResponse(e.getLocalizedMessage(), "Parse Jwt Failed", 401));
            }
        }
    }

    @DeleteMapping(value = "deleteUser/{id}")
    public ResponseEntity<responseData<String>> deleteUser(@RequestHeader("token") String token, @PathVariable("id") Integer id) {
        if (token.length() == 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response.failedResponse("TOKEN EMPTY", "Parse Jwt Failed", 401));
        } else {
            try {
                token = token.split(" ")[1];
                if (checkJwt(token)) {
                    try {
                        userEntity data = userService.getUser(id);
                        userService.deleteUser(id);
                        return ResponseEntity.status(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(response.successResponse(data,"Sukses menghapus pada id: " + id));
                    } catch (Exception e) {
                        return ResponseEntity.status (HttpStatus.INTERNAL_SERVER_ERROR)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(response.failedResponse(e.getLocalizedMessage(),"INTERNAL SERVER ERROR",500));

                    }
                }else{
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(response.failedResponse("Either JWT expired or Invalid.", "Parse Jwt Failed", 401));
                }
            }catch (Exception e){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response.failedResponse(e.getLocalizedMessage(), "Parse Jwt Failed", 401));
            }
        }

    }


}
