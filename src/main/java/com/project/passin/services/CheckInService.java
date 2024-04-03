package com.project.passin.services;

import com.project.passin.domain.attendee.Attendee;
import com.project.passin.domain.checkin.CheckIn;
import com.project.passin.domain.checkin.exception.CheckInAlreadyExistException;
import com.project.passin.repositories.CheckInRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckInService {

    private final CheckInRepository checkInRepository;

    public void registerCheckIn(Attendee attendee){

        this.verifyCheckInExist(attendee.getId());

        CheckIn newCheckIn = new CheckIn();
        newCheckIn.setAttendee(attendee);
        newCheckIn.setCreatedAt(LocalDateTime.now());

        this.checkInRepository.save(newCheckIn);
    }

    private void verifyCheckInExist(String attendeeId){

        Optional<CheckIn> isCheckIn = this.getCheckIn(attendeeId);
        if (isCheckIn.isPresent()) throw new CheckInAlreadyExistException("Attendee already checked in");
    }

    public Optional<CheckIn> getCheckIn(String attendeeId){

        return this.checkInRepository.findByAttendeeId(attendeeId);
    }
}
