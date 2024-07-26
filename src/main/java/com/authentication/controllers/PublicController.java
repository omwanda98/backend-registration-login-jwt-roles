package com.authentication.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
@CrossOrigin("*")
public class PublicController {
    
    @GetMapping("/")
    public String helloUserController(){
        return "Public level access";
    }
}
