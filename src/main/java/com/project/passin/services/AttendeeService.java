package com.project.passin.services;

import com.project.passin.domain.attendee.Attendee;
import com.project.passin.domain.attendee.exceptions.AttendeeAlreadyExistException;
import com.project.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import com.project.passin.domain.checkin.CheckIn;
import com.project.passin.dto.attendee.AttendeeBadgeDto;
import com.project.passin.dto.attendee.AttendeeBadgeResponseDto;
import com.project.passin.dto.attendee.AttendeesDetails;
import com.project.passin.dto.attendee.AttendeesListResponseDto;
import com.project.passin.repositories.AttendeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeService {

    private final AttendeeRepository attendeeRepository;
    private final CheckInService checkInService;

    public List<Attendee> getAllAttendeesFromEvents(String eventId){

        return this.attendeeRepository.findByEventId(eventId);
    }

    public AttendeesListResponseDto getEventsAttendee(String eventId){

        List<Attendee> attendeeList = this.getAllAttendeesFromEvents(eventId);

        List<AttendeesDetails> attendeesDetailsList = attendeeList.stream().map(attendee -> {

            Optional<CheckIn> checkIn = this.checkInService.getCheckIn(attendee.getId());
            LocalDateTime checkedInAt = checkIn.map(CheckIn::getCreatedAt).orElse(null);
            return new AttendeesDetails(attendee.getId(),
                                        attendee.getName(),
                                        attendee.getEmail(),
                                        attendee.getCreatedAt(),
                                        checkedInAt);
        }).toList();
        return new AttendeesListResponseDto(attendeesDetailsList);
    }

    public void verifyAttendeeSubscription(String email, String eventId){

        Optional<Attendee> isAttendeeRegistered = this.attendeeRepository.findByEventIdAndEmail(eventId, email);
        if (isAttendeeRegistered.isPresent()) throw new AttendeeAlreadyExistException("Attendee is already registered");
    }

    public void registerAttendee(Attendee newAttendee){

        this.attendeeRepository.save(newAttendee);
    }

    public AttendeeBadgeResponseDto getAttendeeBadge(String attendeeId, UriComponentsBuilder uriComponentsBuilder){

        Attendee attendee = this.getAttendee(attendeeId);

        var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/check-in").buildAndExpand(attendeeId).toUri().toString();

        AttendeeBadgeDto attendeeBadgeDto = new AttendeeBadgeDto(attendee.getName(), attendee.getEmail(), uri,attendee.getEvent().getId());
        return new AttendeeBadgeResponseDto(attendeeBadgeDto);
    }

    public void checkInAttendee(String attendeeId){

        Attendee attendee = this.getAttendee(attendeeId);
        this.checkInService.registerCheckIn(attendee);
    }

    private Attendee getAttendee(String attendeeId){

        return this.attendeeRepository.findById(attendeeId).orElseThrow(() -> new AttendeeNotFoundException("Attendee not found with id: " + attendeeId));
    }
}