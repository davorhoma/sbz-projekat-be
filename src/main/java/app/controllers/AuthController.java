package app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import app.dtos.AuthResponseDTO;
import app.dtos.UserLoginDTO;
import app.dtos.UserRegisterDTO;
import app.model.User;
import app.services.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterDTO request) {
        try {
            User user = authService.register(
                    request.firstName,
                    request.lastName,
                    request.email,
                    request.password,
                    request.placeId
            );

            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO request) {
        try {
        	AuthResponseDTO user = authService.login(request.email, request.password);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

