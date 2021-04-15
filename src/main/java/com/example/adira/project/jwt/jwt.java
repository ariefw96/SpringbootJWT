package com.example.adira.project.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;


public class jwt {

    String jwt = Jwts.builder().setSubject("Me").signWith(SignatureAlgorithm.HS256,"Hallow").compact();

}
