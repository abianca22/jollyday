package com.application.repository.impl;

import com.application.exceptions.AccessDeniedException;
import com.application.exceptions.UserNotFoundException;
import com.application.model.Group;
import com.application.model.User;
import com.application.model.dto.GroupDTO;
import com.application.repository.CustomGroupRepository;
import com.application.repository.CustomUserRepository;
import com.application.repository.UserRepository;
import com.application.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
public class GroupRepositoryImpl implements CustomGroupRepository {

    private final UserRepository usrRepo;

    @PersistenceContext
    private EntityManager em;

    @Override
    public void updateLeader(Integer groupId, Integer userId) {
        em.createNativeQuery("UPDATE jd_group SET leader_id = ? WHERE id = ?")
                .setParameter(1, userId)
                .setParameter(2, groupId)
                .executeUpdate();
        usrRepo.updateGroup(userId, groupId);
    }

    @Override
    public void updateName(Integer groupId, String groupName) {
        em.createNativeQuery("UPDATE jd_group SET name = ? WHERE id = ?")
                .setParameter(1, groupName)
                .setParameter(2, groupId)
                .executeUpdate();
    }

    @Override
    public void updateDescription(Integer groupId, String groupDesc) {
        em.createNativeQuery("UPDATE jd_group SET description = ? WHERE id = ?")
                .setParameter(1, groupDesc)
                .setParameter(2, groupId)
                .executeUpdate();
    }

    @Override
    public boolean existingLeader(Integer groupId, Integer leaderId) {
        Long alreadyLeader = (Long) em.createNativeQuery("SELECT COUNT(*) FROM jd_group WHERE id <> ? AND leader_id = ?")
                .setParameter(1, groupId)
                .setParameter(2, leaderId)
                .getSingleResult();
        return alreadyLeader == 1;
    }

    @Override
    public boolean existingName(String groupName, Integer groupId) {
        Long nameExists = (Long) em.createNativeQuery("SELECT COUNT(*) FROM jd_group WHERE name = ? AND id <> ?")
                .setParameter(1, groupName)
                .setParameter(2, groupId)
                .getSingleResult();
        return nameExists == 1;
    }

    @Override
    public void setGroupToNull(Integer groupId) {
        em.createNativeQuery("UPDATE jd_event SET collecting_place_id = null WHERE collecting_place_id = ?")
                .setParameter(1, groupId).executeUpdate();
        em.createNativeQuery("UPDATE jd_user SET group_id = null WHERE group_id = ?").setParameter(1, groupId).executeUpdate();

    }

    @Override
    public void deleteGroup(Integer groupId) {
        this.setGroupToNull(groupId);
        em.createNativeQuery("DELETE FROM jd_group WHERE id = ?").setParameter(1, groupId).executeUpdate();
    }


}
