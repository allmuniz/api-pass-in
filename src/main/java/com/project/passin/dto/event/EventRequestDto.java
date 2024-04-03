package com.project.passin.dto.event;

public record EventRequestDto(String title,
                              String details,
                              Integer maximumAttendees) {
}
