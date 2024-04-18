package com.react.demo.controller;

import com.react.demo.dto.CreateAccessTokenRequest;
import com.react.demo.dto.CreateAccessTokenResponse;
import com.react.demo.dto.LoginDto;
import com.react.demo.dto.UserFormDto;
import com.react.demo.service.UserService;
import com.react.demo.service.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final TokenService tokenRefresh;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid UserFormDto userFormDto) {
        try{
            userService.signUp(userFormDto);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> signin(@Valid LoginDto loginDto) {
        try{
            CreateAccessTokenResponse response = userService.signIn(loginDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(CreateAccessTokenRequest request) {
        try{
            CreateAccessTokenResponse response = tokenRefresh.tokenRefresh(request.getRefreshToken());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
        }
    }
}
