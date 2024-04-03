package com.project.passin.dto.event;

import com.project.passin.domain.event.Event;
import lombok.Getter;

@Getter
public class EventResponseDto {

    EventDetailDto event;

    public EventResponseDto(Event event, Integer numberOfAttendees){

        this.event = new EventDetailDto(event.getId(),
                                        event.getTitle(),
                                        event.getDetails(),
                                        event.getSlug(),
                                        event.getMaximumAttendees(),
                                        numberOfAttendees);
    }
}
