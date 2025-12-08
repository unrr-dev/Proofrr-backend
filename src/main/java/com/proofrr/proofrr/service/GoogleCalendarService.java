package com.proofrr.proofrr.service;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleOAuthConstants;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.ConferenceData;
import com.google.api.services.calendar.model.CreateConferenceRequest;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.proofrr.proofrr.dto.CalendarEventResponse;
import com.proofrr.proofrr.dto.CreateMeetingRequest;
import com.proofrr.proofrr.model.User;
import com.proofrr.proofrr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class GoogleCalendarService {

    private final UserRepository userRepository;
    private final NetHttpTransport httpTransport;
    private final GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
    private final GoogleAuthorizationCodeFlow authFlow;
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;

    public GoogleCalendarService(UserRepository userRepository,
                                 @Value("${google.clientId}") String clientId,
                                 @Value("${google.clientSecret}") String clientSecret,
                                 @Value("${google.calendar.redirectUri:http://localhost:8081/api/calendar/oauth2/callback}") String redirectUri)
            throws GeneralSecurityException, IOException {
        this.userRepository = userRepository;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        this.authFlow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport,
                jsonFactory,
                clientId,
                clientSecret,
                List.of(CalendarScopes.CALENDAR_EVENTS))
                .setAccessType("offline")
                .build();
    }

    public String buildAuthorizationUrl(String userEmail) {
        if (userEmail == null || userEmail.isBlank()) {
            throw new IllegalArgumentException("User email is required for authorization");
        }

        return authFlow.newAuthorizationUrl()
                .setRedirectUri(redirectUri)
                .setState(userEmail)
                .setAccessType("offline")
                .set("prompt", "consent")
                .build();
    }

    public void exchangeCodeForTokens(String code, String stateEmail) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Missing authorization code");
        }
        if (stateEmail == null || stateEmail.isBlank()) {
            throw new IllegalArgumentException("Missing user email in OAuth state");
        }

        User user = userRepository.findByEmail(stateEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found for email " + stateEmail));

        try {
            TokenResponse tokenResponse = authFlow.newTokenRequest(code)
                    .setRedirectUri(redirectUri)
                    .execute();

            user.setCalendarAccessToken(tokenResponse.getAccessToken());
            user.setCalendarRefreshToken(tokenResponse.getRefreshToken());
            if (tokenResponse.getExpiresInSeconds() != null) {
                user.setCalendarTokenExpiry(Instant.now().plusSeconds(tokenResponse.getExpiresInSeconds()));
            }
            userRepository.save(user);
        } catch (IOException ex) {
            throw new IllegalArgumentException("Failed to exchange authorization code for tokens");
        }
    }

    public CalendarEventResponse createMeet(CreateMeetingRequest request) {
        User user = userRepository.findByEmail(request.getUserEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + request.getUserEmail()));

        Credential credential = ensureCredential(user);

        Calendar calendar = new Calendar.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName("proofrr")
                .build();

        Event event = new Event()
                .setSummary(request.getSummary())
                .setDescription(request.getDescription());

        DateTime start = new DateTime(request.getStartDateTime());
        DateTime end = new DateTime(request.getEndDateTime());

        event.setStart(new EventDateTime().setDateTime(start).setTimeZone(request.getTimeZone()));
        event.setEnd(new EventDateTime().setDateTime(end).setTimeZone(request.getTimeZone()));

        if (request.getAttendees() != null && !request.getAttendees().isEmpty()) {
            List<EventAttendee> attendees = request.getAttendees().stream()
                    .map(email -> new EventAttendee().setEmail(email))
                    .toList();
            event.setAttendees(attendees);
        }

        event.setConferenceData(new ConferenceData()
                .setCreateRequest(new CreateConferenceRequest().setRequestId(UUID.randomUUID().toString())));

        try {
            Event created = calendar.events()
                    .insert("primary", event)
                    .setConferenceDataVersion(1)
                    .execute();
            return new CalendarEventResponse(created.getId(), created.getHangoutLink());
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to create Google Calendar event with Meet link");
        }
    }

    private Credential ensureCredential(User user) {
        if (user.getCalendarRefreshToken() == null) {
            throw new IllegalArgumentException("User has not granted Google Calendar access");
        }

        Credential credential = new Credential.Builder(BearerToken.authorizationHeaderAccessMethod())
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setTokenServerUrl(new GenericUrl(GoogleOAuthConstants.TOKEN_SERVER_URL))
                .setClientAuthentication(new ClientParametersAuthentication(clientId, clientSecret))
                .build();

        credential.setAccessToken(user.getCalendarAccessToken());
        credential.setRefreshToken(user.getCalendarRefreshToken());
        if (user.getCalendarTokenExpiry() != null) {
            credential.setExpirationTimeMilliseconds(user.getCalendarTokenExpiry().toEpochMilli());
        }

        try {
            Long expiresInSeconds = credential.getExpiresInSeconds();
            if (expiresInSeconds == null || expiresInSeconds <= 60) {
                boolean refreshed = credential.refreshToken();
                if (!refreshed) {
                    throw new IllegalArgumentException("Unable to refresh Google access token");
                }
                user.setCalendarAccessToken(credential.getAccessToken());
                if (credential.getRefreshToken() != null) {
                    user.setCalendarRefreshToken(credential.getRefreshToken());
                }
                if (credential.getExpirationTimeMilliseconds() != null) {
                    user.setCalendarTokenExpiry(Instant.ofEpochMilli(credential.getExpirationTimeMilliseconds()));
                }
                userRepository.save(user);
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to refresh Google credentials", ex);
        }

        return credential;
    }
}
