package com.example.Clone.Insta.Backend.Controller;

import com.example.Clone.Insta.Backend.DTO.LoginRequestDto;
import com.example.Clone.Insta.Backend.DTO.LoginResponseDto;
import com.example.Clone.Insta.Backend.DTO.SignupRequestDto;
import com.example.Clone.Insta.Backend.DTO.UsernameCheckResponseDto;
import com.example.Clone.Insta.Backend.Security.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignupRequestDto signupRequestDto){
        return ResponseEntity.ok(authService.signup(signupRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto){
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }

    @GetMapping("/username/check")
    public UsernameCheckResponseDto checkUsername(
            @RequestParam String username) {

        return authService.checkUsername(username);
    }

}
