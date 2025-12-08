package com.proofrr.proofrr.service;

import com.proofrr.proofrr.dto.GoogleAuthRequest;
import com.proofrr.proofrr.dto.LoginRequest;
import com.proofrr.proofrr.dto.SignupRequest;
import com.proofrr.proofrr.model.AuthProvider;
import com.proofrr.proofrr.model.User;
import com.proofrr.proofrr.repository.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final GoogleIdTokenVerifier googleIdTokenVerifier;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, GoogleIdTokenVerifier googleIdTokenVerifier) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.googleIdTokenVerifier = googleIdTokenVerifier;
    }

    @Transactional
    public User register(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setProvider(AuthProvider.LOCAL);

        return userRepository.save(user);
    }

    public User login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (user.getProvider() != AuthProvider.LOCAL) {
            throw new IllegalArgumentException("Use Google sign in for this account");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return user;
    }

    @Transactional
    public User loginWithGoogle(GoogleAuthRequest request) {
        GoogleIdToken idToken = verify(request.getIdToken());
        GoogleIdToken.Payload payload = idToken.getPayload();

        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String providerId = payload.getSubject(); // stable Google user id

        return upsertGoogleUser(name, email, providerId);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User upsertGoogleUser(String fullName, String email, String providerId) {
        return userRepository.findByEmail(email)
                .map(existing -> updateGoogleInfo(existing, fullName, providerId))
                .orElseGet(() -> createGoogleUser(fullName, email, providerId));
    }

    private User createGoogleUser(String fullName, String email, String providerId) {
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setProvider(AuthProvider.GOOGLE);
        user.setProviderId(providerId);
        return userRepository.save(user);
    }

    private User updateGoogleInfo(User user, String fullName, String providerId) {
        user.setFullName(fullName);
        user.setProvider(AuthProvider.GOOGLE);
        user.setProviderId(providerId);
        return userRepository.save(user);
    }

    private GoogleIdToken verify(String idToken) {
        try {
            GoogleIdToken token = googleIdTokenVerifier.verify(idToken);
            if (token == null) {
                throw new IllegalArgumentException("Invalid Google token");
            }
            return token;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Failed to verify Google token");
        }
    }
}
