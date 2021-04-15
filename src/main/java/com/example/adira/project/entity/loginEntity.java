package com.example.adira.project.entity;

import javax.persistence.*;

@Entity
@Table(name="admin")

public class loginEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String username;
    public String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
