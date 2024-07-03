package com.application.service.impl;

import com.application.exceptions.AccessDeniedException;
import com.application.exceptions.UserNotFoundException;
import com.application.model.*;
import com.application.model.dto.EventDTO;
import com.application.model.dto.UserDTO;
import com.application.repository.GroupRepository;
import com.application.repository.UserRepository;
import com.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repo;
    private final GroupRepository groupRepo;
    @Override
    public Optional<User> findUserById(Integer userId) {
        return repo.findById(userId);
    }

    @Override
    public void deleteUserById(Integer userId) {
        repo.deleteById(userId);
    }

    @Override
    public User addNewUser(User userToAdd) {
        if (userToAdd.getUserRole() == null)
            userToAdd.setUserRole(Role.USER);
        return repo.save(userToAdd);
    }

    @Override
    public void addJoinedEventToUser(Integer userId, Integer joinedEventId) {
        repo.addJoinedEventToUser(userId, joinedEventId);
    }

    @Override
    public void modifyUserLastName(Integer userId, String lastName) throws AccessDeniedException {

        if(currentUserHasGivenId(userId))
            repo.updateLastName(userId, lastName);
        else throw new AccessDeniedException();
    }

    @Override
    public void modifyUserFirstName(Integer userId, String firstName) throws AccessDeniedException {
        if(currentUserHasGivenId(userId))
            repo.updateFirstName(userId, firstName);
        else throw new AccessDeniedException();
    }

    @Override
    public void modifyUserBirthday(Integer userId, LocalDate birthday) throws AccessDeniedException {
        if(currentUserHasGivenId(userId))
            repo.updateBirthday(userId, birthday);
        else throw new AccessDeniedException();
    }

    @Override
    public void changeUserGroup(Integer userId, Integer groupId) throws AccessDeniedException {
        if (!this.currentUserIsAdministrator()) throw new AccessDeniedException();
        repo.updateGroup(userId, groupId);
    }

    @Override
    public void leaveGroup(Integer userId) throws AccessDeniedException, UserNotFoundException {
        if (!this.currentUserIsAdministrator() && !this.getAuthenticatedUserId().equals(userId)) throw new AccessDeniedException();
        var usr = this.findUserById(userId).orElseThrow(UserNotFoundException::new);
        if (usr.getUserRole() == Role.EDITOR)
        {
            this.changeUserRole(userId, Role.USER);
            groupRepo.updateLeader(usr.getGroup().getId(), null);
        }
        repo.leaveGroup(userId);
    }
    @Override
    public void changeUserRole(Integer userId, Role role) throws AccessDeniedException {
        if(!currentUserIsAdministrator()) throw new AccessDeniedException();
        repo.updateRole(userId, role);
    }

    @Override
    public boolean checkIfUserIsInAGroup(Integer userId) {
        return repo.getGroupId(userId) != null;
    }

    @Override
    public void addCelebratedFriendToUser(Integer userId, Integer friendId) throws Exception {
        if (currentUserHasGivenId(userId)) repo.addCelebratedUser(userId, friendId);
        else throw new AccessDeniedException();
    }

    @Override
    public void removeCelebratedFriendFromUser(Integer userId, Integer friendId) throws AccessDeniedException {
        if(currentUserHasGivenId(userId)) repo.removeCelebratedUser(userId, friendId);
        else throw new AccessDeniedException();
    }
    @Override
    public List<UserDTO> findAllCelebratedFriends(Integer userId) {
        return repo.findAllCelebratedFriends(userId);
    }

    @Override
    public Integer getAuthenticatedUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth);
        System.out.println(auth.isAuthenticated());
        if(auth != null && auth.isAuthenticated()) {
                User currentUser = (auth.getPrincipal() instanceof User) ? (User) auth.getPrincipal() : null;
                return currentUser != null ? currentUser.getId() : null;
        }
        return null;
    }

    @Override
    public boolean currentUserIsAdministrator() {
        Integer authUsr = getAuthenticatedUserId();
        return repo.findById(authUsr).get().getUserRole().equals(Role.ADMIN);
    }

    @Override
    public boolean currentUserIsEditor() {
        Integer authUsr = getAuthenticatedUserId();
        return repo.findById(authUsr).get().getUserRole().equals(Role.EDITOR);
    }

    @Override
    public boolean currentUserHasGivenId(Integer userId) {
        Integer authUsr = getAuthenticatedUserId();
        return repo.findById(authUsr).get().getId().equals(userId);
    }

    @Override
    public Optional<User> findUserByUsername(String usrnm) {
        return repo.findByUsername(usrnm);
    }

    @Override
    public List<User> findAllUsers() {
        return repo.findAll();
    }

    @Override
    public List<User> findUsersByGroupId(Integer groupId) {
        return repo.findUsersByGroup(groupId);
    }

    @Override
    public List<Event> findAllEvents(Integer userId) {
        List<Event> events = new ArrayList<>();
        List<EventDTO> eventDTOs = repo.findAllEvents(userId);
        for (EventDTO eventDTO : eventDTOs) {
            User collector = repo.findById(eventDTO.getCollectorId()).orElse(null);
            User friend = repo.findById(eventDTO.getCelebratedUserId()).orElse(null);
            Group grp = groupRepo.findById(eventDTO.getCollectingPlaceId()).orElse(null);
            Event ev = Event.builder()
                    .id(eventDTO.getId())
                    .celebratedUser(friend)
                    .collectorUser(collector)
                    .creationDate(eventDTO.getCreationDate())
                    .collectedAmount(eventDTO.getCollectedAmount())
                    .collectingPlace(grp)
                    .build();
            events.add(ev);
        }
        return events;
    }

    @Override
    public void joinGroupRequest(Integer userId, Integer groupId) throws AccessDeniedException {
        if (!this.getAuthenticatedUserId().equals(userId)) {
            throw new AccessDeniedException();
        }
        repo.joinGroupRequest(userId, groupId);
    }

    @Override
    public void updateJoinStatus(Integer userId, JoinStatus joinStatus) throws AccessDeniedException {
        if (!this.currentUserIsAdministrator()) {
            throw new AccessDeniedException();
        }
        repo.updateJoinStatus(userId, joinStatus);
    }
}
