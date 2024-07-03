package com.application.repository;

import com.application.model.Event;
import com.application.model.User;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface CustomEventRepository {
    Long countParticipants(Integer userId);

    Long countCollectingEvents(Integer userId);

    boolean checkIfEventExists(Integer userId);

    void updateCollectedAmount(Integer eventId, Double amount);

    List<Integer> getAllParticipantIds(Integer celebratedId);

    Integer getEvent(Integer collector, Integer user, LocalDate creationDate, Integer group);
}
