package com.application.repository.impl;

import com.application.model.JoinStatus;
import com.application.model.Role;
import com.application.model.User;
import com.application.model.dto.EventDTO;
import com.application.model.dto.UserDTO;
import com.application.repository.CustomUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.mapping.Join;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        em.createNativeQuery("UPDATE jd_user SET group_id = ?, join_status = ? where id = ?")
                .setParameter(1, groupId)
                .setParameter(2, JoinStatus.ACCEPTED.toString())
                .setParameter(3, userId)
                .executeUpdate();
    }

    @Override
    public void updateJoinStatus(Integer userId, JoinStatus joinStatus) {
        em.createNativeQuery("UPDATE jd_user SET join_status = ? where id = ?")
                .setParameter(1, joinStatus.toString())
                .setParameter(2, userId)
                .executeUpdate();
    }

    @Override
    public void joinGroupRequest(Integer userId, Integer groupId) {
        em.createNativeQuery("UPDATE jd_user SET group_id = ?, join_status = ? where id = ?")
                .setParameter(1, groupId)
                .setParameter(2, JoinStatus.PENDING.toString())
                .setParameter(3, userId)
                .executeUpdate();
    }

    @Override
    public void leaveGroup(Integer userId) {
        em.createNativeQuery("UPDATE jd_user SET group_id = null, join_status = ? where id = ?")
                .setParameter(1, JoinStatus.NONE.toString())
                .setParameter(2, userId)
                .executeUpdate();
    }

    @Override
    public boolean addCelebratedUser(Integer userId, Integer friendId) throws Exception {
        if (userId.equals(friendId)) throw new Exception("Cannot join the same id");
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
        em.createNativeQuery("UPDATE jd_user SET user_role = ? where id = ?")
                .setParameter(1, role.toString())
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

    public List<User> findUsersByGroup(Integer groupId) {
        List<Object[]> usersInGroup = em.createNativeQuery("SELECT username, last_name, first_name, id, join_status FROM jd_user WHERE group_id = ? ORDER BY join_status DESC, username ASC LIMIT 50").setParameter(1, groupId)
                .setParameter(1, groupId)
                .getResultList();
        List<User> users = new ArrayList<>();
        for(Object[] obj: usersInGroup) {
            JoinStatus joinStatus = null;
            if (obj[4].equals("PENDING")) joinStatus = JoinStatus.PENDING;
            if (obj[4].equals("ACCEPTED")) joinStatus = JoinStatus.ACCEPTED;
            if (obj[4].equals("NONE")) joinStatus = JoinStatus.NONE;
            User user = User.builder()
                    .username(obj[0].toString())
                    .lastName(obj[1].toString())
                    .firstName(obj[2].toString())
                    .id((Integer) obj[3])
                    .joinStatus(joinStatus)
                    .build();
            users.add(user);
        }
        return users;
    }

    @Override
    public List<EventDTO> findAllEvents(Integer userId) {
        List<Object[]> userEvents = em.createNativeQuery("SELECT id, collected_amount, creation_date, celebrated_user_id, collecting_place_id, collector_id FROM jd_event, joins WHERE joins.participant_id = ? AND joins.event_id = jd_event.id").setParameter(1, userId).getResultList();
        List<EventDTO> events = new ArrayList<>();
        for(Object[] obj: userEvents) {
            EventDTO ev = EventDTO.builder()
                    .id((Integer) obj[0])
                    .collectedAmount((Double) obj[1])
                    .creationDate(LocalDate.parse(obj[2].toString()))
                    .celebratedUserId((Integer) obj[3])
                    .collectingPlaceId((Integer) obj[4])
                    .collectorId((Integer) obj[5])
                    .build();
            events.add(ev);
        }
        return events;
    }

}
