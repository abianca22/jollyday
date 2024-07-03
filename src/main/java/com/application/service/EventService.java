package com.application.service;

import com.application.exceptions.TooFewParticipants;
import com.application.exceptions.UserNotFoundException;
import com.application.exceptions.UserNotInGroup;
import com.application.model.Event;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EventService {

    Integer selectCollectorId(Integer celebratedId) throws TooFewParticipants;

    Integer addEvent(Integer celebratedId, Double amount) throws UserNotInGroup, TooFewParticipants, UserNotFoundException;

    List<Event> findAllEventsByCelebratedId(Integer celebratedId);

    LocalDate createDate(LocalDate bday);

    Integer findEventId(Integer collectorId, Integer userId, LocalDate createDate, Integer groupId);
    Optional<Event> findById(Integer id);
}
