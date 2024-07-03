package com.application.controller;

import com.application.exceptions.AccessDeniedException;
import com.application.exceptions.GroupNameAlreadyExists;
import com.application.exceptions.LeaderAlreadyExistsForAnotherGroup;
import com.application.exceptions.UserNotFoundException;
import com.application.model.Group;
import com.application.model.Role;
import com.application.model.User;
import com.application.model.dto.GroupDTO;
import com.application.service.GroupService;
import com.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jollyday/groups")
@CrossOrigin(origins = "http://localhost:4300")
@Validated
public class GroupsController {
    private final GroupService groupService;
    private final UserService userService;

    @GetMapping("/all")
    public List<Group> getAllExistingGroups() {
        try {
            return groupService.getAllGroups();
        }
        catch(Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @GetMapping("/{name}")
    public ResponseEntity<GroupDTO> getGroupByName(@PathVariable String name) {
        var grp = groupService.getGroupByName(name).orElse(null);
        GroupDTO dto = null;
        if (grp != null && grp.getLeader() != null)
            dto = GroupDTO.builder().name(grp.getName()).description(grp.getDescription()).leaderId(grp.getLeader().getId()).build();
        else if (grp != null)
            dto = GroupDTO.builder().name(grp.getName()).description(grp.getDescription()).build();
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{name}/update")
    public ResponseEntity<String> updateGroup(@PathVariable String name, @RequestBody GroupDTO group) throws Exception {
        try {
            Group grp = groupService.getGroupByName(name).orElseThrow(() -> new Exception("Group not found"));
            if (!grp.getName().equals(group.getName()) && group.getName() != null) {
                groupService.updateNameOfGroup(grp.getId(), group.getName());
            }
            if (!grp.getDescription().equals(group.getDescription()) && group.getDescription() != null) {
                groupService.updateDescriptionOfGroup(grp.getId(), group.getDescription());
            }
            if ((grp.getLeader() == null || !Objects.equals(grp.getLeader().getId(), group.getLeaderId())) && group.getLeaderId() != null) {
                groupService.updateLeaderOfGroup(grp.getId(), group.getLeaderId());
            }
            return ResponseEntity.ok("The group has been updated");
        }
        catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/addGroup")
    public ResponseEntity<Group> addGroup(@Valid @RequestBody GroupDTO group) throws Exception {
        groupService.addGroup(group);
        return ResponseEntity.ok(groupService.getGroupByName(group.getName()).orElseThrow(() -> new Exception("Group not found!")));
    }

    @GetMapping("/{groupId}/members")
    public List<User> getUsersFromGroup(@PathVariable Integer groupId) {
        return userService.findUsersByGroupId(groupId);
    }

    @GetMapping("/{groupId}/name")
    public String getGroupName(@PathVariable Integer groupId) {
        Group grp = groupService.getGroupById(groupId).orElse(null);
        return grp != null ? grp.getName() : null;
    }

    @GetMapping("/{name}/id")
    public Integer getGroupIdByName(@PathVariable String name) {
        Group grp = groupService.getGroupByName(name).orElse(null);
        return grp != null ? grp.getId() : null;
    }

    @PutMapping("/{groupId}/updateLeaderTo/{userId}")
    public Group updateLeader(@PathVariable Integer groupId, @PathVariable Integer userId) throws AccessDeniedException, LeaderAlreadyExistsForAnotherGroup {
        groupService.updateLeaderOfGroup(groupId, userId);
        return groupService.getGroupById(groupId).orElse(null);
    }

    @PutMapping("/{groupId}/removeLeaderTitle/{userId}")
    public Group removeLeaderTitle(@PathVariable Integer groupId, @PathVariable Integer userId) throws AccessDeniedException, LeaderAlreadyExistsForAnotherGroup {
        groupService.updateLeaderOfGroup(groupId, null);
        userService.changeUserRole(userId, Role.USER);
        return groupService.getGroupById(groupId).orElse(null);
    }

    @DeleteMapping("/deleteGroup/{groupId}")
    public void deleteGroup(@PathVariable Integer groupId) throws AccessDeniedException, UserNotFoundException {
        groupService.deleteGroup(groupId);
    }
}
