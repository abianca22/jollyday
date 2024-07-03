package com.application.repository.impl;

import com.application.model.Event;
import com.application.repository.CustomEventRepository;
import com.application.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.hibernate.metamodel.model.domain.TupleType;
import org.springframework.cglib.core.Local;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
public class EventRepositoryImpl implements CustomEventRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Long countParticipants(Integer userId) {
        Long nrParticipants = (Long) em.createNativeQuery("SELECT COUNT(*) FROM participates_for WHERE celebrated_id = ?")
                .setParameter(1, userId)
                .getSingleResult();
        return nrParticipants;
    }

    @Override
    public Long countCollectingEvents(Integer userId) {
        Long nrEvents = (Long) em.createNativeQuery("SELECT COUNT(*) FROM jd_event WHERE collector_id = ?")
                .setParameter(1, userId)
                .getSingleResult();
        return nrEvents;
    }

    @Override
    public boolean checkIfEventExists(Integer userId) {
        List<Object[]> existingEventsForUser = em.createNativeQuery("SELECT creation_date FROM jd_event WHERE celebrated_id = ? ORDER BY creation_date desc")
                .setParameter(1, userId)
                .getResultList();
        LocalDate creationDate = (LocalDate) existingEventsForUser.get(0)[0];
        LocalDate beforeDate = LocalDate.now().minusWeeks(2).minusDays(1);
        return creationDate.isAfter(beforeDate);
    }

    @Override
    public void updateCollectedAmount(Integer eventId, Double amount) {
        em.createNativeQuery("UPDATE jd_event SET collected_amount = ? WHERE id = ?")
                .setParameter(1, amount)
                .setParameter(2, eventId);
    }

    @Override
    public List<Integer> getAllParticipantIds(Integer celebratedId) {
        List<Integer> ids = new ArrayList<>();
        List<Object> participantIds = em.createNativeQuery("SELECT participant_id FROM participates_for WHERE celebrated_id = ?")
                .setParameter(1, celebratedId)
                .getResultList();
        for(Object obj: participantIds) {
            ids.add((Integer) obj);
        }
        return ids;
    }

    @Override
    public Integer getEvent(Integer collector, Integer user, LocalDate creationDate, Integer group) {
        try{
            System.out.println(
                    "Parametrii : " + collector + ", " + user + ", " + creationDate + ", " + group
            );
            var id = (Integer) em.createNativeQuery("SELECT id FROM jd_event WHERE collector_id = ? AND celebrated_user_id = ? AND creation_date = ?")
                    .setParameter(1, collector)
                    .setParameter(2, user)
                    .setParameter(3, creationDate)
                    .getSingleResult();
            System.out.println("Id eveniment: " + id);
            return id;
        }
        catch(NoResultException e) {
            return null;
        }
    }


}
