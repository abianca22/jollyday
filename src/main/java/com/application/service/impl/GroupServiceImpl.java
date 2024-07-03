package com.application.service.impl;

import com.application.exceptions.AccessDeniedException;
import com.application.exceptions.GroupNameAlreadyExists;
import com.application.exceptions.LeaderAlreadyExistsForAnotherGroup;
import com.application.exceptions.UserNotFoundException;
import com.application.model.Group;
import com.application.model.Role;
import com.application.model.User;
import com.application.model.dto.GroupDTO;
import com.application.repository.GroupRepository;
import com.application.repository.UserRepository;
import com.application.service.GroupService;
import com.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository grpRepo;

    private final UserRepository usrRepo;

    private final UserService usrService;

    @Override
    public boolean groupExists(GroupDTO group) {
        return grpRepo.findByName(group.getName()).isPresent();
    }

    @Override
    public boolean isAlreadyLeader(Integer leaderId, Integer groupId) {
        return grpRepo.existingLeader(groupId, leaderId);
    }

    @Override
    public boolean nameAlreadyExists(String groupName, Integer groupId) {
        return grpRepo.existingName(groupName, groupId);
    }

    @Override
    public void updateLeaderOfGroup(Integer groupId, Integer leaderId) throws AccessDeniedException, LeaderAlreadyExistsForAnotherGroup {
        if (!usrService.currentUserIsAdministrator()) throw new AccessDeniedException();
        if (isAlreadyLeader(leaderId, groupId)) throw new LeaderAlreadyExistsForAnotherGroup();
        var grp = grpRepo.findById(groupId).orElse(null);
        if (grp != null && grp.getLeader() != null && !grp.getLeader().getId().equals(leaderId)) {
            usrService.changeUserRole(grp.getLeaderId(), Role.USER);
        }
        usrService.changeUserRole(leaderId, Role.EDITOR);
        grpRepo.updateLeader(groupId, leaderId);
    }

    @Override
    public void updateNameOfGroup(Integer groupId, String groupName) throws AccessDeniedException, GroupNameAlreadyExists {
        if (!usrService.currentUserIsAdministrator() && !usrService.currentUserIsEditor()) throw new AccessDeniedException();
        if (grpRepo.existingName(groupName, groupId)) throw new GroupNameAlreadyExists();
        grpRepo.updateName(groupId, groupName);
    }

    @Override
    public void updateDescriptionOfGroup(Integer groupId, String groupDesc) {
        grpRepo.updateDescription(groupId, groupDesc);
    }

    @Override
    public void deleteGroupById(Integer groupId) throws AccessDeniedException {
        if (!usrService.currentUserIsAdministrator()) throw new AccessDeniedException();
        grpRepo.deleteById(groupId);
    }

    @Override
    public List<Group> getAllGroups() {
        return grpRepo.findAll();
    }

    @Override
    public Optional<Group> getGroupByName(String name) {
        return grpRepo.findByName(name);
    }

    @Override
    public Optional<Group> getGroupById(Integer id) {
        return grpRepo.findById(id);
    }

    // doar adminul poate adauga
    @Override
    public void addGroup(GroupDTO group) throws AccessDeniedException, UserNotFoundException, GroupNameAlreadyExists {
        if(!usrService.currentUserIsAdministrator()) throw new AccessDeniedException();
        Group groupToAdd;
        if (group.getLeaderId() != null) {
            User user = null;
            var optUsr = usrRepo.findById(group.getLeaderId());
            if (optUsr.isPresent())
                user = optUsr.get();
            else throw new UserNotFoundException();
            if (this.groupExists(group)) throw new GroupNameAlreadyExists();
            groupToAdd = Group.builder()
                    .name(group.getName())
                    .description(group.getDescription())
                    .leader(user)
                    .build();
        }
        else {
            if (this.groupExists(group)) throw new GroupNameAlreadyExists();
            groupToAdd = Group.builder()
                    .name(group.getName())
                    .description(group.getDescription())
                    .build();
        }
        grpRepo.save(groupToAdd);
    }

    @Override
    public Optional<Group> findGroupByLeader(User leader) {
        return grpRepo.findByLeader(leader);
    }

    @Override
    public void deleteGroup(Integer groupId) throws AccessDeniedException {
        if (!this.usrService.currentUserIsAdministrator()) throw new AccessDeniedException();
        grpRepo.deleteGroup(groupId);
    }

}
