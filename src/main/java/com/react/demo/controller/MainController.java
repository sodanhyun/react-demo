package com.react.demo.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @CrossOrigin("http://localhost:3000")
    @GetMapping("/api/main")
    public String getMain() {
        return "Hello World";
    }

}
