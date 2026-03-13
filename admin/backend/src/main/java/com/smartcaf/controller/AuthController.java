package com.smartcaf.controller;

import com.smartcaf.model.User;
import com.smartcaf.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email déjà utilisé"));
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("CLIENT");
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "Inscription réussie"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginUser) {
        Optional<User> userOpt = userRepository.findByEmail(loginUser.getEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("message", "Email ou mot de passe incorrect"));
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(loginUser.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("message", "Email ou mot de passe incorrect"));
        }

        // TODO: Générer un JWT pour l'utilisateur
        // (Pour l’instant on renvoie l’utilisateur + un message, en JSON)
        return ResponseEntity.ok(Map.of(
            "message", "Connexion réussie",
            "user", user
        ));
    }
}
