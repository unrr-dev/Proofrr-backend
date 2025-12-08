package com.proofrr.proofrr.controller;

import com.proofrr.proofrr.dto.CalendarEventResponse;
import com.proofrr.proofrr.dto.CreateMeetingRequest;
import com.proofrr.proofrr.service.GoogleCalendarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/calendar")
@Tag(name = "Calendar", description = "Google Calendar and Meet integration")
public class CalendarController {

    private final GoogleCalendarService googleCalendarService;

    public CalendarController(GoogleCalendarService googleCalendarService) {
        this.googleCalendarService = googleCalendarService;
    }

    @GetMapping("/authorize")
    @Operation(summary = "Start Google Calendar OAuth flow", description = "Returns a Google consent URL for the provided email")
    public ResponseEntity<Map<String, String>> authorize(@RequestParam("email") String email,
                                                         Authentication authentication) {
        if (authentication != null && !authentication.getName().equalsIgnoreCase(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Email must match the authenticated user"));
        }
        String url = googleCalendarService.buildAuthorizationUrl(email);
        return ResponseEntity.ok(Map.of("authorizationUrl", url));
    }

    @GetMapping("/oauth2/callback")
    @Operation(summary = "OAuth2 callback", description = "Google redirects here with the authorization code")
    public ResponseEntity<Map<String, String>> callback(@RequestParam("code") String code,
                                                        @RequestParam("state") String state) {
        googleCalendarService.exchangeCodeForTokens(code, state);
        return ResponseEntity.ok(Map.of("message", "Google Calendar connected for " + state));
    }

    @PostMapping("/meetings")
    @Operation(summary = "Create a Calendar event with a Meet link", description = "Uses stored Google tokens for the user email")
    public ResponseEntity<CalendarEventResponse> createMeeting(@Valid @RequestBody CreateMeetingRequest request,
                                                               Authentication authentication) {
        if (authentication != null && !authentication.getName().equalsIgnoreCase(request.getUserEmail())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        CalendarEventResponse response = googleCalendarService.createMeet(request);
        return ResponseEntity.ok(response);
    }
}
