package com.application.repository.impl;

import com.application.model.Role;
import com.application.model.User;
import com.application.model.dto.UserDTO;
import com.application.repository.CustomUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
public class UserRepositoryImpl implements CustomUserRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void addJoinedEventToUser(Integer userId, Integer joinedEventId) {
        em.createNativeQuery("INSERT INTO joins (participant_id, event_id) values (?, ?)")
                .setParameter(1, userId)
                .setParameter(2, joinedEventId)
                .executeUpdate();
    }

    @Override
    public void updateLastName(Integer userId, String lastName) {
        em.createNativeQuery("UPDATE jd_user SET last_name = ? where id = ?")
                .setParameter(1, lastName)
                .setParameter(2, userId)
                .executeUpdate();
    }

    @Override
    public void updateFirstName(Integer userId, String firstName) {
        em.createNativeQuery("UPDATE jd_user SET first_name = ? where id = ?")
                .setParameter(1, firstName)
                .setParameter(2, userId)
                .executeUpdate();
    }

    @Override
    public void updateBirthday(Integer userId, LocalDate birthday) {
        em.createNativeQuery("UPDATE jd_user SET birthday = ? where id = ?")
                .setParameter(1, birthday)
                .setParameter(2, userId)
                .executeUpdate();
    }

    @Override
    public void updateGroup(Integer userId, Integer groupId) {
        em.createNativeQuery("UPDATE jd_user SET group_id = ? where id = ?")
                .setParameter(1, groupId)
                .setParameter(2, userId)
                .executeUpdate();
    }

    @Override
    public boolean addCelebratedUser(Integer userId, Integer friendId) {
        var ifExists = (Long) em.createNativeQuery("SELECT COUNT(*) FROM participates_for WHERE participant_id = ? AND celebrated_id = ?")
                        .setParameter(1, userId)
                        .setParameter(2, friendId)
                        .getSingleResult();
        if (ifExists == 0) {
            em.createNativeQuery("INSERT INTO participates_for (participant_id, celebrated_id) values (?, ?)")
                    .setParameter(1, userId)
                    .setParameter(2, friendId)
                    .executeUpdate();
            return true;
        }
        else return false;
    }

    @Override
    public void removeCelebratedUser(Integer userId, Integer friendId) {
        em.createNativeQuery("DELETE FROM participates_for WHERE participant_id = ? AND celebrated_id = ?")
                .setParameter(1, userId)
                .setParameter(2, friendId)
                .executeUpdate();
    }

    @Override
    public void updateRole(Integer userId, Role role) {
        em.createNativeQuery("UPDATE jd_user SET role = ? where id = ?")
                .setParameter(1, role)
                .setParameter(2, userId)
                .executeUpdate();
    }

    @Override
    public Integer getGroupId(Integer userId) {
        var usrGrp = em.createNativeQuery("SELECT group_id FROM jd_user WHERE id = ?").setParameter(1, userId).getSingleResult();
        return usrGrp != null ? ((Integer) usrGrp):  null;
    }

    @Override
    public List<UserDTO> findAllCelebratedFriends(Integer userId) {
        List<Object[]> friends =  em.createNativeQuery("SELECT username, last_name, first_name, email, birthday, group_id FROM participates_for, jd_user WHERE celebrated_id = jd_user.id AND participant_id = ?").setParameter(1, userId).getResultList();
        List<UserDTO> celebratedFriends = new ArrayList<>();
        for(Object[] obj: friends) {
            var grpid = obj[5] != null ? obj[5].toString() : null;
            UserDTO userDTO = UserDTO.builder()
                    .username(obj[0].toString())
                    .lastName(obj[1].toString())
                    .firstName(obj[2].toString())
                    .email(obj[3].toString())
                    .birthday(LocalDate.parse(obj[4].toString()))
                    .groupId(Integer.getInteger(grpid))
                    .build();
            celebratedFriends.add(userDTO);
        }
        return celebratedFriends;
    }

    @Override
    public Integer countCollectingEvents(Integer userId) {
        List<Object[]> collectingEvents = em.createNativeQuery("SELECT * FROM jd_event WHERE collector_id = ?")
                .setParameter(1, userId)
                .getResultList();
        return collectingEvents.size();
    }

}
