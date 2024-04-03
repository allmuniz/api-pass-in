package com.project.passin.repositories;

import com.project.passin.domain.attendee.Attendee;
import com.project.passin.domain.checkin.CheckIn;
import lombok.Getter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CheckInRepository extends JpaRepository<CheckIn, Integer> {

    Optional<CheckIn> findByAttendeeId(String attendeeId);
}
