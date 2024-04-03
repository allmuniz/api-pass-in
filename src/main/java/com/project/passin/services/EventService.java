package com.project.passin.services;

import com.project.passin.domain.attendee.Attendee;
import com.project.passin.domain.event.Event;
import com.project.passin.domain.event.exceptions.EventFullException;
import com.project.passin.domain.event.exceptions.EventNotFoundException;
import com.project.passin.dto.attendee.AttendeeIdDto;
import com.project.passin.dto.attendee.AttendeeRequestDto;
import com.project.passin.dto.event.EventIdDto;
import com.project.passin.dto.event.EventRequestDto;
import com.project.passin.dto.event.EventResponseDto;
import com.project.passin.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private  final AttendeeService attendeeService;

    public EventResponseDto getEventDetail(String eventId){

        Event event = this.getEventById(eventId);

        List<Attendee> attendeeList = attendeeService.getAllAttendeesFromEvents(eventId);

        return new EventResponseDto(event, attendeeList.size());
    }

    public EventIdDto createEvent(EventRequestDto eventDto){

        Event newEvent = new Event();
        newEvent.setTitle(eventDto.title());
        newEvent.setDetails(eventDto.details());
        newEvent.setMaximumAttendees(eventDto.maximumAttendees());
        newEvent.setSlug(this.createSlug(eventDto.title()));

        this.eventRepository.save(newEvent);

        return new EventIdDto(newEvent.getId());
    }

    public AttendeeIdDto registerAttendeeOnEvent(String eventId, AttendeeRequestDto attendeeRequestDto){

        this.attendeeService.verifyAttendeeSubscription(attendeeRequestDto.email(), eventId);
        Event event = this.getEventById(eventId);
        List<Attendee> attendeeList = attendeeService.getAllAttendeesFromEvents(eventId);

        if (event.getMaximumAttendees() <= attendeeList.size()) throw new EventFullException("Event is full");

        Attendee newAttendee = new Attendee();
        newAttendee.setName(attendeeRequestDto.name());
        newAttendee.setEmail(attendeeRequestDto.email());
        newAttendee.setEvent(event);
        newAttendee.setCreatedAt(LocalDateTime.now());
        this.attendeeService.registerAttendee(newAttendee);

        return new AttendeeIdDto(newAttendee.getId());
    }

    private Event getEventById(String eventId){

        return this.eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event not found with Id: " + eventId));
    }

    private String createSlug(String text){

        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{InCOMBINING_DIACRITICAL_MARKS}", "")
                        .replaceAll("[^\\w\\s]", "")
                        .replaceAll("\\s+", "-")
                        .toLowerCase();
    }
}
