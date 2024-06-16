package com.application.repository;

import com.application.model.Role;
import com.application.model.User;
import com.application.model.dto.UserDTO;

import java.time.LocalDate;
import java.util.List;

public interface CustomUserRepository {
    void addJoinedEventToUser(Integer userId, Integer joinedEventId);

    void updateLastName(Integer userId, String lastName);

    void updateFirstName(Integer userId, String firstName);

    void updateBirthday(Integer userId, LocalDate birthday);

    void updateGroup(Integer userId, Integer groupId);

    boolean addCelebratedUser(Integer userId, Integer friendId);

    void removeCelebratedUser(Integer userId, Integer friendId);

    void updateRole(Integer userId, Role role);

    Integer getGroupId(Integer userId);

    List<UserDTO> findAllCelebratedFriends(Integer userId);

    Integer countCollectingEvents(Integer userId);
}
