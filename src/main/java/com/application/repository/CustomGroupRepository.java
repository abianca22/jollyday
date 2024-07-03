package com.application.repository;


import com.application.exceptions.UserNotFoundException;
import com.application.model.dto.GroupDTO;

public interface CustomGroupRepository {
    void updateLeader(Integer groupId, Integer userId);

    void updateName(Integer groupId, String groupName);

    void updateDescription(Integer groupId, String groupDesc);

    boolean existingLeader(Integer groupId, Integer leaderId);

    boolean existingName(String name, Integer groupId);

    void setGroupToNull(Integer groupId);

    void deleteGroup(Integer groupId);
}
