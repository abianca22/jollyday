package com.application.service.impl;

import com.application.exceptions.TooFewParticipants;
import com.application.exceptions.UserNotFoundException;
import com.application.exceptions.UserNotInGroup;
import com.application.model.Event;
import com.application.model.User;
import com.application.repository.EventRepository;
import com.application.repository.UserRepository;
import com.application.service.EventService;
import com.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository evRepo;
    private final UserRepository usrRepo;

    private final UserService usrSrv;

    @Override
    public Integer selectCollectorId(Integer celebratedId) throws TooFewParticipants {
        Integer celGrp = usrRepo.getGroupId(celebratedId);
        List<Integer> possibleCollectors = evRepo.getAllParticipantIds(celebratedId).stream().filter(id -> id != celebratedId && (usrRepo.getGroupId(id) != celGrp || (usrRepo.getGroupId(id) == null && celGrp == null))).collect(Collectors.toList());
        Integer nrCollectors = possibleCollectors.size();
        if (nrCollectors < 2) throw new TooFewParticipants();
        Random rndGen = new Random();
        return rndGen.nextInt(nrCollectors);
    }

    // eventul nu va fi creat cu mai mult de 2 saptamani inainte de ziua de nastere
    @Override
    public void addEvent(Integer celebratedId, Double amount) throws UserNotInGroup, TooFewParticipants, UserNotFoundException {
        if(evRepo.getAllParticipantIds(celebratedId).size() < 2) throw new TooFewParticipants();
        Optional<User> celUsr = usrSrv.findUserById(celebratedId);
        Integer collectorId = selectCollectorId(celebratedId);
        Optional<User> colUsr = usrSrv.findUserById(collectorId);
        if (celUsr.isEmpty() || colUsr.isEmpty()) throw new UserNotFoundException();
        LocalDate birthday = celUsr.get().getBirthday();
        Integer month = birthday.getMonth().getValue();
        Integer year = month < LocalDate.now().getMonth().getValue() ? LocalDate.now().getYear() + 1 : LocalDate.now().getYear();
        Event eventToAdd = Event.builder()
                .celebratedUser(celUsr.get())
                .collectorUser(colUsr.get())
                .collectedAmount(amount)
                .collectingPlace(colUsr.get().getGroup())
                .creationDate(LocalDate.of(year, birthday.getMonth(), birthday.getDayOfMonth()))
                .build();
        evRepo.save(eventToAdd);
    }
}
