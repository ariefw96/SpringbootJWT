package com.example.adira.project.service;

import com.example.adira.project.repository.userRepository;
import com.example.adira.project.entity.userEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class userService {

    @Autowired
    userRepository user;

    public void addNewUser(userEntity param){
        user.save(param);
    }

    public List<userEntity> getAllUser(){
        return user.findAll();
    }

    public userEntity getUser(Integer id){
        return user.findById(id).get();
    }

    public void updateUser(userEntity param, Integer id){
        userEntity data = user.findById(id).get();
        param.id = id;
        if(param.email == null){
            param.setEmail(data.email);
        }
        if(param.name == null){
            param.setName(data.name);
        }
        if(param.phone == null){
            param.setPhone(data.phone);
        }
        user.save(param);
    }

    public userEntity deleteUser(Integer id){
        userEntity data = user.findById(id).get();
        user.deleteById(id);
        return data;
    }

}
