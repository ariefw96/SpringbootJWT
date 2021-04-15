package com.example.adira.project.entity;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class privateKeyEntity {
    public String privateKey;
    public String secretKey;

    public privateKeyEntity() {
        this.privateKey = "WANGYWANGY";
        this.secretKey  = Base64.getEncoder().encodeToString(privateKey.getBytes());
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
