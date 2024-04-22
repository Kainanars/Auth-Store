package com.example.auth.services;

import com.example.auth.exceptions.UserAlreadyExistsException;
import com.example.auth.dtos.AuthenticationDTO;
import com.example.auth.dtos.RegisterDTO;
import com.example.auth.entities.User;
import com.example.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User register(RegisterDTO data) {
        if (userRepository.findByLogin(data.login()) != null) {
            throw new UserAlreadyExistsException("User with this login already exists.");
        }

        String encryptedPassword = passwordEncoder.encode(data.password());
        User newUser = new User(data.login(), encryptedPassword, data.role());
        return userRepository.save(newUser);
    }

    public User login(AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = authenticationManager.authenticate(usernamePassword);
        return (User) auth.getPrincipal();
    }

}
