package com.proofrr.proofrr.dto;

public class CalendarEventResponse {
    private String eventId;
    private String meetLink;

    public CalendarEventResponse(String eventId, String meetLink) {
        this.eventId = eventId;
        this.meetLink = meetLink;
    }

    public String getEventId() {
        return eventId;
    }

    public String getMeetLink() {
        return meetLink;
    }
}
