package com.proofrr.proofrr.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider provider = AuthProvider.LOCAL;

    @Column(length = 200)
    private String providerId;

    @Column(length = 2048)
    private String calendarAccessToken;

    @Column(length = 512)
    private String calendarRefreshToken;

    private Instant calendarTokenExpiry;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AuthProvider getProvider() {
        return provider;
    }

    public void setProvider(AuthProvider provider) {
        this.provider = provider;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getCalendarAccessToken() {
        return calendarAccessToken;
    }

    public void setCalendarAccessToken(String calendarAccessToken) {
        this.calendarAccessToken = calendarAccessToken;
    }

    public String getCalendarRefreshToken() {
        return calendarRefreshToken;
    }

    public void setCalendarRefreshToken(String calendarRefreshToken) {
        this.calendarRefreshToken = calendarRefreshToken;
    }

    public Instant getCalendarTokenExpiry() {
        return calendarTokenExpiry;
    }

    public void setCalendarTokenExpiry(Instant calendarTokenExpiry) {
        this.calendarTokenExpiry = calendarTokenExpiry;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
