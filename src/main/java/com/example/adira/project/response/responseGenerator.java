package com.example.adira.project.response;

import org.springframework.stereotype.Component;

@Component

public class responseGenerator <T> {

    public <T> responseData <T> successResponse(T data, String message){

        responseData response = new responseData <>();
        response.setData(data);
        response.setStatus(200);
        response.setMessage(message);

        return response;
    }

}
