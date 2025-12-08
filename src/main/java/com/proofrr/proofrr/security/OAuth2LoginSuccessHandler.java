package com.proofrr.proofrr.security;

import com.proofrr.proofrr.model.User;
import com.proofrr.proofrr.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.time.Duration;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final String frontendUrl;
    private final boolean cookieSecure;
    private final long jwtExpirationMs;

    public OAuth2LoginSuccessHandler(JwtService jwtService,
                                     UserRepository userRepository,
                                     @Value("${app.frontend-url}") String frontendUrl,
                                     @Value("${jwt.expiration-ms:86400000}") long jwtExpirationMs,
                                     @Value("${server.ssl.enabled:false}") boolean cookieSecure) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.frontendUrl = frontendUrl.endsWith("/") ? frontendUrl.substring(0, frontendUrl.length() - 1) : frontendUrl;
        this.cookieSecure = cookieSecure;
        this.jwtExpirationMs = jwtExpirationMs;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String email = extractEmail(authentication);
        String token = "";
        if (email != null) {
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                token = jwtService.generateToken(user);
                writeTokenCookie(response, token);
            }
        }
        String redirect = buildRedirectUrl(email, token);
        response.sendRedirect(redirect);
    }

    private String extractEmail(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof DefaultOAuth2User principal)) {
            return null;
        }
        Map<String, Object> attributes = principal.getAttributes();
        Object email = attributes.get("email");
        return email != null ? email.toString() : null;
    }

    private String buildRedirectUrl(String email, String token) {
        StringBuilder sb = new StringBuilder(frontendUrl).append("?");
        if (token != null && !token.isBlank()) {
            sb.append("token=").append(urlEncode(token));
        }
        if (email != null && !email.isBlank()) {
            if (sb.charAt(sb.length() - 1) != '?') {
                sb.append("&");
            }
            sb.append("email=").append(urlEncode(email));
        }
        return sb.toString();
    }

    private String urlEncode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private void writeTokenCookie(HttpServletResponse response, String token) {
        if (token == null || token.isBlank()) {
            return;
        }
        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .sameSite("Lax")
                .maxAge(Duration.ofMillis(jwtExpirationMs))
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }
}
