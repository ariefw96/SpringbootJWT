package com.example.adira.project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "data not found on the server.")

public class notfoundException extends RuntimeException {

    Integer status;
    String message;

    public notfoundException(Integer status, String message){
        super(message);
        this.status = status;
        this.message= message;
    }

}
