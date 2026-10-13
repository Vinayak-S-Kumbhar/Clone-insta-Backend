package com.example.Clone.Insta.Backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UsernameCheckResponseDto {

    private boolean valid;
    private boolean available;
    private String message;
    private List<String> suggestions;
}