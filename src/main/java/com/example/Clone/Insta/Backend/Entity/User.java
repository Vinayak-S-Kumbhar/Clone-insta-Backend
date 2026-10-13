package com.example.Clone.Insta.Backend.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    private String id;

    private String email;

    private String name;

    @Column(nullable = false, unique = true, length = 30)
    private String username;

    private String password;

    private String dateOfBirth;

    private LocalDateTime createdAt;
}
