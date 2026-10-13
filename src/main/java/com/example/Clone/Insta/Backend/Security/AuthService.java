package com.example.Clone.Insta.Backend.Security;

import com.example.Clone.Insta.Backend.DTO.LoginRequestDto;
import com.example.Clone.Insta.Backend.DTO.LoginResponseDto;
import com.example.Clone.Insta.Backend.DTO.SignupRequestDto;
import com.example.Clone.Insta.Backend.DTO.UsernameCheckResponseDto;
import com.example.Clone.Insta.Backend.Entity.User;
import com.example.Clone.Insta.Backend.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

//    public String signup(SignupRequestDto signupRequestDto){
//        User user = userRepository.findByUsername(signupRequestDto.getUsername()).orElse(null);
//
//        if(user != null){
//            throw new RuntimeException("User Already exist With this UserId " + signupRequestDto.getUsername());
//        }
//
//        User newUser = User.builder()
//                .id(UUID.randomUUID().toString())
//                .email(signupRequestDto.getEmail())
//                .name(signupRequestDto.getName())
//                .dateOfBirth(signupRequestDto.getDateOfBirth())
//                .username(signupRequestDto.getUsername())
//                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
//                .createdAt(LocalDateTime.now())
//                .build();
//
//        userRepository.save(newUser);
//
//        return "Account Created Successfully!";
//    }
public String signup(SignupRequestDto signupRequestDto) {

    String rawUsername = signupRequestDto.getUsername();

    String username = rawUsername == null
            ? ""
            : rawUsername.trim().toLowerCase(Locale.ROOT);

    if (!USERNAME_PATTERN.matcher(username).matches()) {
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Username must contain 3-30 letters, numbers, periods, or underscores."
        );
    }

    if (userRepository.existsByUsernameIgnoreCase(username)) {
        throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "The username " + username + " is not available."
        );
    }

    User newUser = User.builder()
            .id(UUID.randomUUID().toString())
            .email(signupRequestDto.getEmail())
            .name(signupRequestDto.getName())
            .dateOfBirth(signupRequestDto.getDateOfBirth())
            .username(username)
            .password(passwordEncoder.encode(
                    signupRequestDto.getPassword()
            ))
            .createdAt(LocalDateTime.now())
            .build();

    userRepository.save(newUser);

    return "Account Created Successfully!";
}

    public LoginResponseDto login(LoginRequestDto loginRequestDto){

        User user = userRepository.findByUsername(loginRequestDto.getUsername()).orElseThrow(() ->
                new RuntimeException("User not found With this username " + loginRequestDto.getUsername()));

        if(!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())){
            throw new RuntimeException("Password not Match");
        }

        String accessToken = jwtUtils.genrateToken(user);
        return new LoginResponseDto(accessToken, user.getId());
    }


    private static final Pattern USERNAME_PATTERN =
            Pattern.compile("^[a-z0-9._]{3,30}$");

    public UsernameCheckResponseDto checkUsername(String rawUsername) {

        if (rawUsername == null || rawUsername.trim().isEmpty()) {
            return new UsernameCheckResponseDto(
                    false,
                    false,
                    "Username is required.",
                    Collections.emptyList()
            );
        }

        String username = rawUsername.trim().toLowerCase(Locale.ROOT);

        // Allow 3-30 letters, numbers, periods and underscores.
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            return new UsernameCheckResponseDto(
                    false,
                    false,
                    "Use 3-30 letters, numbers, periods, or underscores.",
                    Collections.emptyList()
            );
        }

        // Check whether the username exists in the database.
        if (!userRepository.existsByUsernameIgnoreCase(username)) {
            return new UsernameCheckResponseDto(
                    true,
                    true,
                    "Username is available!",
                    Collections.emptyList()
            );
        }

        // The username is already taken.
        return new UsernameCheckResponseDto(
                true,
                false,
                "The username " + username + " is not available.",
                generateSuggestions(username)
        );
    }

    private List<String> generateSuggestions(String username) {

        List<String> suggestions = new ArrayList<>();

        // Leave enough room for a four-digit suffix.
        String base = username.substring(
                0, Math.min(username.length(), 26)
        );

        int attempts = 0;

        while (suggestions.size() < 3 && attempts < 100) {

            attempts++;

            int number = ThreadLocalRandom.current()
                    .nextInt(1000, 10000);

            String suggestion = base + number;

            if (!userRepository.existsByUsernameIgnoreCase(suggestion)
                    && !suggestions.contains(suggestion)) {

                suggestions.add(suggestion);
            }
        }

        return suggestions;
    }
}
