package com.application.service.impl;

import com.application.exceptions.TooFewParticipants;
import com.application.exceptions.UserNotFoundException;
import com.application.exceptions.UserNotInGroup;
import com.application.model.Event;
import com.application.model.User;
import com.application.model.Group;
import com.application.repository.EventRepository;
import com.application.repository.UserRepository;
import com.application.service.EventService;
import com.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
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
        List<Integer> possibleCollectors = evRepo.getAllParticipantIds(celebratedId).stream().filter(id -> !Objects.equals(id, celebratedId) && (!Objects.equals(usrRepo.getGroupId(id), celGrp) || usrRepo.getGroupId(id) == null)).toList();
        Integer nrCollectors = possibleCollectors.size();
        if (nrCollectors < 2) {
            possibleCollectors = evRepo.getAllParticipantIds(celebratedId).stream().filter(id -> !Objects.equals(id, celebratedId)).toList();
            System.out.println(possibleCollectors);
            nrCollectors = possibleCollectors.size();
            if (possibleCollectors.size() < 2) throw new TooFewParticipants();
        }
        Random rndGen = new Random();
        return rndGen.nextInt(nrCollectors);
    }

    // eventul nu va fi creat cu mai mult de 2 saptamani inainte de ziua de nastere
    @Override
    public Integer addEvent(Integer celebratedId, Double amount) throws UserNotFoundException {
        if(evRepo.getAllParticipantIds(celebratedId).size() < 2) return null;
        Optional<User> celUsr = usrSrv.findUserById(celebratedId);
        System.out.println("Sarbatorit " + celUsr.get().getId());
        try {
            var participants = evRepo.getAllParticipantIds(celebratedId);
            Integer collectorId = participants.get(selectCollectorId(celebratedId));
            Optional<User> colUsr = usrSrv.findUserById(collectorId);
            System.out.println("Colector: " + collectorId);
            System.out.println("Colector: " + colUsr.get());
            if (celUsr.isEmpty() || colUsr.isEmpty()) return null;
            LocalDate birthday = celUsr.get().getBirthday();
            Integer month = birthday.getMonth().getValue();
            Integer year = month < LocalDate.now().getMonth().getValue() ? LocalDate.now().getYear() + 1 : LocalDate.now().getYear();
            Event eventToAdd = Event.builder()
                    .celebratedUser(celUsr.get())
                    .collectorUser(colUsr.get())
                    .collectedAmount(amount)
                    .collectingPlace(colUsr.get().getGroup().getId().equals(celUsr.get().getGroup().getId()) ? null : colUsr.get().getGroup())
                    .creationDate(LocalDate.of(year, birthday.getMonth(), birthday.getDayOfMonth()))
                    .build();
            System.out.println("Eveniment: " + eventToAdd.toString());
            evRepo.save(eventToAdd);
            return this.findEventId(colUsr.get().getId(), celUsr.get().getId(), this.createDate(celUsr.get().getBirthday()), colUsr.get().getGroup() != null ? colUsr.get().getGroup().getId() : null);
        }
        catch (TooFewParticipants e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public LocalDate createDate(LocalDate birthday) {
        Integer month = birthday.getMonth().getValue();
        Integer year = month < LocalDate.now().getMonth().getValue() ? LocalDate.now().getYear() + 1 : LocalDate.now().getYear();
        return LocalDate.of(year, month, birthday.getDayOfMonth());
    }

    @Override
    public Integer findEventId(Integer collectorId, Integer userId, LocalDate createDate, Integer groupId) {
        return evRepo.getEvent(collectorId, userId, createDate, groupId);
    }

    @Override
    public Optional<Event> findById(Integer id) {
        return evRepo.findById(id);
    }

    @Override
    public List<Event> findAllEventsByCelebratedId(Integer celebratedId) {
        var usr = usrSrv.findUserById(celebratedId).orElse(null);
        if (usr == null) return new ArrayList<>();
        return evRepo.findAllByCelebratedUser(usr);
    }
}
