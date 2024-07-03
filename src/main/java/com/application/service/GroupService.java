package com.application.service;

import com.application.exceptions.AccessDeniedException;
import com.application.exceptions.GroupNameAlreadyExists;
import com.application.exceptions.LeaderAlreadyExistsForAnotherGroup;
import com.application.exceptions.UserNotFoundException;
import com.application.model.Group;
import com.application.model.User;
import com.application.model.dto.GroupDTO;


import java.util.List;
import java.util.Optional;


public interface GroupService {

    Optional<Group> getGroupById(Integer id);

    void addGroup(GroupDTO group) throws AccessDeniedException, UserNotFoundException, GroupNameAlreadyExists;

    Optional<Group> findGroupByLeader(User leader);

    boolean groupExists(GroupDTO group);

    boolean isAlreadyLeader(Integer leaderId, Integer groupId);

    boolean nameAlreadyExists(String groupName, Integer groupId);
    void updateLeaderOfGroup(Integer groupId, Integer leaderId) throws AccessDeniedException, LeaderAlreadyExistsForAnotherGroup;

    void updateNameOfGroup(Integer groupId, String groupName) throws AccessDeniedException, GroupNameAlreadyExists;

    void updateDescriptionOfGroup(Integer groupId, String groupDesc);

    void deleteGroupById(Integer groupId) throws AccessDeniedException;

    List<Group> getAllGroups();

    Optional<Group> getGroupByName(String name);

    void deleteGroup(Integer id) throws AccessDeniedException;

}
