package com.application.controller.demo;

import com.application.exceptions.AccessDeniedException;
import com.application.exceptions.UserNotFoundException;
import com.application.exceptions.UserNotInGroup;
import com.application.model.User;
import com.application.model.dto.GroupDTO;
import com.application.model.dto.UserDTO;
import com.application.repository.GroupRepository;
import com.application.repository.UserRepository;
import com.application.service.GroupService;
import com.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jollyday/demo")
@RequiredArgsConstructor
public class DemoController {
    private final UserService usrService;

    private final UserRepository usrRepo;

    private final GroupRepository grpRepo;

    private final GroupService grpService;
    @GetMapping
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello from secured endpoint!");
    }
    @GetMapping("/{userId}/groupIdIsNotNull")
    public boolean hasGroupId(@PathVariable Integer userId) {
        return usrService.checkIfUserIsInAGroup(userId);
    }

    @GetMapping("/{userId}/groupId")
    public Integer getGroupId(@PathVariable Integer userId) {
        return usrRepo.getGroupId(userId);
    }

    @PutMapping("/{userId}/changeLastName/{lastName}")
    public ResponseEntity<String> updateName(@PathVariable Integer userId, @PathVariable String lastName) throws UserNotFoundException, AccessDeniedException {
        usrService.modifyUserLastName(userId, lastName);
        User user = usrRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("Could not find user with the given user id!"));
        return ResponseEntity.ok(user.toString());
    }

    @PostMapping("/{userId}/wantsToParticipateFor/{friendId}")
    public List<UserDTO> insertParticipateFor(@PathVariable Integer userId, @PathVariable Integer friendId) throws Exception {
        User user = usrRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("Could not find user with the given id!"));
        User friend = usrRepo.findById(friendId).orElseThrow(() -> new UserNotFoundException("Could not find user with the given id!"));
        usrService.addCelebratedFriendToUser(userId, friendId);
        return usrService.findAllCelebratedFriends(userId);
    }

    @GetMapping("/currentUser")
    public Integer getCurrentUserId() {
        return usrService.getAuthenticatedUserId();
    }

    @PostMapping("/addGroup/{name}/{desc}/{leaderId}")
    public ResponseEntity<String> addGroup (@PathVariable String name, @PathVariable String desc, @PathVariable Integer leaderId){
        try {
            GroupDTO groupDTO = GroupDTO.builder()
                    .name(name)
                    .description(desc)
                    .leaderId(leaderId)
                    .build();
            grpService.addGroup(groupDTO);
            var usr = usrService.findUserById(leaderId);
            if(usr.isEmpty())
                throw new UserNotFoundException();
            System.out.println(usr);
            var grp = grpService.findGroupByLeader(usr.get());
            if(grp.isEmpty())
                throw new Exception("Group was not created");
            System.out.println(grp);
            return ResponseEntity.ok(grp.get().toString());
        }
        catch(Exception err) {
            System.out.println(err.getMessage());
            return new ResponseEntity<>(err.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
        }
    }
}
