package com.example.auth.dtos;

import com.example.auth.entities.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {
}
