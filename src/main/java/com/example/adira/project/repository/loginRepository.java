package com.example.adira.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.adira.project.entity.loginEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface loginRepository extends JpaRepository <loginEntity, Integer> {

    @Query(value = "SELECT * FROM admin WHERE username = ? AND password = ?",nativeQuery = true)
    public loginEntity login(@Param("username") String username, @Param("password") String password);

}
