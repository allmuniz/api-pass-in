package com.project.passin.controllers;

import com.project.passin.dto.attendee.AttendeeIdDto;
import com.project.passin.dto.attendee.AttendeeRequestDto;
import com.project.passin.dto.attendee.AttendeesListResponseDto;
import com.project.passin.dto.event.EventIdDto;
import com.project.passin.dto.event.EventRequestDto;
import com.project.passin.dto.event.EventResponseDto;
import com.project.passin.services.AttendeeService;
import com.project.passin.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final AttendeeService attendeeService;

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDto> getEvent(@PathVariable String id){

        EventResponseDto event = this.eventService.getEventDetail(id);
        return  ResponseEntity.ok(event);
    }

    @PostMapping
    public  ResponseEntity<EventIdDto> createEvent(@RequestBody EventRequestDto body, UriComponentsBuilder uriComponentsBuilder){

        EventIdDto eventIdDto = this.eventService.createEvent(body);
        var uri = uriComponentsBuilder.path("/events/{id}").buildAndExpand(eventIdDto.eventId()).toUri();
        return ResponseEntity.created(uri).body(eventIdDto);
    }

    @GetMapping("/attendees/{id}")
    public ResponseEntity<AttendeesListResponseDto> getEventsAttendees(@PathVariable String id){

        AttendeesListResponseDto attendeesListResponse = this.attendeeService.getEventsAttendee(id);
        return  ResponseEntity.ok(attendeesListResponse);
    }

    @PostMapping("/{eventId}/attendees")
    public ResponseEntity<AttendeeIdDto> registerParticipant(@PathVariable String eventId, @RequestBody AttendeeRequestDto body, UriComponentsBuilder uriComponentsBuilder){

        AttendeeIdDto attendeeIdDto = this.eventService.registerAttendeeOnEvent(eventId, body);
        var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/badge}").buildAndExpand(attendeeIdDto.attendeeId()).toUri();
        return ResponseEntity.created(uri).body(attendeeIdDto);
    }
}
