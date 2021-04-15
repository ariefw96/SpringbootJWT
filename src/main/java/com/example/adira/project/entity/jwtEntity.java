package com.example.adira.project.entity;

import javax.persistence.Entity;

import com.example.adira.project.entity.privateKeyEntity;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

@Entity
public class jwtEntity {

    public  String jwt;

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public boolean returnJwt (String jwt){
        privateKeyEntity key = new privateKeyEntity();
        try{
            Jwts.parser().setSigningKey(key.getSecretKey()).parse(jwt);
            return true;
        }catch (JwtException e){
            return false;
        }
    }
}
