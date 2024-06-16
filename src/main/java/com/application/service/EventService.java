package com.application.service;

import com.application.exceptions.TooFewParticipants;
import com.application.exceptions.UserNotFoundException;
import com.application.exceptions.UserNotInGroup;

public interface EventService {

    Integer selectCollectorId(Integer celebratedId) throws TooFewParticipants;

    void addEvent(Integer celebratedId, Double amount) throws UserNotInGroup, TooFewParticipants, UserNotFoundException;
}
