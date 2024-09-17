package com.namlato.laptopshop.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/user")
    public String getUser() {
        return "Only user can access this page";
    }

    @GetMapping("/admin")
    public String getAdmin() {
        return "Only admin can access this page";
    }
}
