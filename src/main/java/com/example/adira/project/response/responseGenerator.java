package com.example.adira.project.response;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;

@Component
public class responseGenerator <T> {

    public <T> responseData <T> successResponse(T data, String message){

        responseData response = new responseData <>();
        response.setData(data);
        response.setStatus(200);
        response.setMessage(message);

        return response;
    }

    public <T> responseData <T> failedResponse(T data, String message, Integer status){
        responseData response = new responseData <>();
        response.setStatus(status);
        response.setMessage(message);
        response.setData(data);

        return response;
    }

}
