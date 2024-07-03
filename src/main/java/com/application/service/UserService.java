package com.application.service;

import com.application.exceptions.AccessDeniedException;
import com.application.exceptions.UserNotFoundException;
import com.application.model.Event;
import com.application.model.JoinStatus;
import com.application.model.Role;
import com.application.model.User;
import com.application.model.dto.UserDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findUserById(Integer userId);

    void deleteUserById(Integer userId);

    User addNewUser(User userToAdd);

    void addJoinedEventToUser(Integer userId, Integer joinedEventId);

    void modifyUserLastName(Integer userId, String lastName) throws AccessDeniedException;

    void modifyUserFirstName(Integer userId, String firstName) throws AccessDeniedException;

    void modifyUserBirthday(Integer userId, LocalDate birthday) throws AccessDeniedException;

    void changeUserGroup(Integer userId, Integer groupId) throws AccessDeniedException;

    void leaveGroup(Integer userId) throws AccessDeniedException, UserNotFoundException;

    void changeUserRole(Integer userId, Role role) throws AccessDeniedException;

    boolean checkIfUserIsInAGroup(Integer userId);

    void addCelebratedFriendToUser(Integer userId, Integer friendId) throws Exception;

    void removeCelebratedFriendFromUser(Integer userId, Integer friendId) throws AccessDeniedException;

    List<UserDTO> findAllCelebratedFriends(Integer userId);

    Integer getAuthenticatedUserId();

    boolean currentUserIsAdministrator();

    boolean currentUserIsEditor();

    boolean currentUserHasGivenId(Integer userId);

    Optional<User> findUserByUsername(String usrnm);

    List<User> findAllUsers();

    List<User> findUsersByGroupId(Integer groupId);

    List<Event> findAllEvents(Integer userId);

    void joinGroupRequest(Integer userId, Integer groupId) throws AccessDeniedException;

    void updateJoinStatus(Integer userId, JoinStatus joinStatus) throws AccessDeniedException;

    Optional<User> findUserByEmail(String email);

    void deleteUser(Integer userId);
}
