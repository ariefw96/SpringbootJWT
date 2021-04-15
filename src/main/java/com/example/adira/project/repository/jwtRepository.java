package com.example.adira.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.adira.project.entity.jwtEntity;

@Repository
public interface jwtRepository extends JpaRepository <jwtEntity, String> {

}
