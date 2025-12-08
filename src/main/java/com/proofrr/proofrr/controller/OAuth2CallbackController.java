package com.proofrr.proofrr.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/oauth2")
public class OAuth2CallbackController {

    @GetMapping("/success")
    public ResponseEntity<Map<String, String>> success() {
        return ResponseEntity.ok(Map.of("message", "Google login successful"));
    }

    @GetMapping("/failure")
    public ResponseEntity<Map<String, String>> failure(String error) {
        String message = error != null ? error : "OAuth2 login failed";
        return ResponseEntity.badRequest().body(Map.of("message", message));
    }
}
