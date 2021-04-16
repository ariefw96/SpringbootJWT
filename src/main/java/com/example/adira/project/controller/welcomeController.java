package com.example.adira.project.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/v1")

public class welcomeController {

    @GetMapping(value = "")
    public String Home(){
        return "API LOADED";
    }

}
