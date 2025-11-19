package com.ecommerce.app.controller;

import com.ecommerce.app.dto.Response;
import com.ecommerce.app.dto.UserDto;
import com.ecommerce.app.entity.UserEntity;
import com.ecommerce.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Response<UserDto>> register(@RequestBody UserEntity user) {
        Response<UserDto> response = userService.register(user);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Response<UserDto>> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        Response<UserDto> response = userService.login(email, password);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
