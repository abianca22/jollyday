package com.application.controller;

import com.application.exceptions.AccessDeniedException;
import com.application.exceptions.UserNotFoundException;
import com.application.exceptions.UserNotInGroup;
import com.application.model.Event;
import com.application.model.User;
import com.application.model.dto.UserDTO;
import com.application.security.config.JwtService;
import com.application.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jollyday/users")
@CrossOrigin(origins = "http://localhost:4300")
public class UsersController {
    private final UserService usrSrv;

    private final PasswordEncoder pwEnc;

    private String userToJson(User usr) {
        DateTimeFormatter dt = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String bday = usr.getBirthday().format(dt);
        String grp = (usr.getGroup() != null) ? usr.getGroup().getName() : "Nu face parte dintr-un grup!";
        String json = String.format("{\"username\": \"%s\", \"lastName\": \"%s\", \"firstName\": \"%s\", \"email\": \"%s\", \"birthday\": \"%s\", \"groupName\": \"%s\", \"userRole\": \"%s\", \"joinStatus\": \"%s\"}",
                usr.getUsername(),
                usr.getLastName(),
                usr.getFirstName(),
                usr.getEmail(),
                bday,
                grp,
                usr.getUserRole().toString(),
                usr.getJoinStatus());
        return json;
    }

    @GetMapping("/{username}")
    public String getGivenUser(@PathVariable String username) throws UserNotFoundException {
        User usr = usrSrv.findUserByUsername(username).orElseThrow(UserNotFoundException::new);
        System.out.println(usr);
        return userToJson(usr);
    }

    @GetMapping("/getUser/{id}")
    public String getUserById(@PathVariable Integer id) throws UserNotFoundException {
        User usr = usrSrv.findUserById(id).orElseThrow(UserNotFoundException::new);
        return userToJson(usr);
    }

    @GetMapping("/all")
    public List<User> getAllExistingUsers() {
        try {
            System.out.println(usrSrv.getAuthenticatedUserId());
            return usrSrv.findAllUsers();
        }
        catch(Exception error) {
            return new ArrayList<>();
        }
    }

    @DeleteMapping("/users/{username}")
    public ResponseEntity<Map<String, Boolean>> deleteUser(@PathVariable String username) throws UserNotFoundException {
        User usr = usrSrv.findUserByUsername(username).orElseThrow(UserNotFoundException::new);
        usrSrv.deleteUserById(usr.getId());
        Map<String, Boolean> res = new HashMap<>();
        res.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(res);
    }


    @PostMapping("/users")
    public User addUser(@RequestBody User usr) {
        usr.setPassword(pwEnc.encode(usr.getPassword()));
        return usrSrv.addNewUser(usr);
    }

    @PutMapping("/updateUser/{username}")
    public ResponseEntity<User> updateUser(@PathVariable String username, @RequestBody User reqUsr) throws UserNotFoundException {
        User usr = usrSrv.findUserByUsername(username).orElseThrow(UserNotFoundException::new);
        usr.setUsername(reqUsr.getUsername());
        usr.setFirstName(reqUsr.getFirstName());
        usr.setLastName((reqUsr.getLastName()));
        usr.setEmail(reqUsr.getEmail());
        usr.setPassword(pwEnc.encode(reqUsr.getPassword()));
        usr.setUserRole(reqUsr.getUserRole());
        usr.setGroup(reqUsr.getGroup());
        usr.setBirthday(reqUsr.getBirthday());
        User modUsr = usrSrv.addNewUser(usr);
        return ResponseEntity.ok(modUsr);
    }

    @GetMapping("/getCurrentUser")
    public String currentUser() throws UserNotFoundException {
        var usrId = this.usrSrv.getAuthenticatedUserId();
        var usr = this.usrSrv.findUserById(usrId).orElseThrow(UserNotFoundException::new);
        return userToJson(usr);
    }

    @GetMapping("/getFriends")
    public List<UserDTO> getFriends() {
        var usrId = this.usrSrv.getAuthenticatedUserId();
        return this.usrSrv.findAllCelebratedFriends(usrId);
    }

    @GetMapping("/getEvents")
    public List<Event> getEvents() {
        var usrId = this.usrSrv.getAuthenticatedUserId();
        return this.usrSrv.findAllEvents(usrId);
    }

    @PostMapping("/participatesFor/{friendId}")
    public List<UserDTO> insertParticipateFor(@PathVariable Integer friendId) throws Exception {
        var userId = usrSrv.getAuthenticatedUserId();
        User friend = usrSrv.findUserById(friendId).orElseThrow(() -> new UserNotFoundException("Could not find user with the given id!"));
        usrSrv.addCelebratedFriendToUser(userId, friendId);
        return usrSrv.findAllCelebratedFriends(userId);
    }

    @DeleteMapping("/stopParticipating/{friendId}")
    public List<UserDTO> deleteFriend(@PathVariable Integer friendId) throws Exception {
        var userId = usrSrv.getAuthenticatedUserId();
        usrSrv.removeCelebratedFriendFromUser(userId, friendId);
        return usrSrv.findAllCelebratedFriends(userId);
    }

    @PutMapping("/updateGroup/{groupId}/{userId}")
    public void updateGroup(@PathVariable Integer groupId, @PathVariable Integer userId) throws Exception {
        usrSrv.changeUserGroup(userId, groupId);
    }

    @PutMapping("/joinGroupRequest/{groupId}")
    public void joinGroupRequest(@PathVariable Integer groupId) throws Exception {
        var userId = usrSrv.getAuthenticatedUserId();
        usrSrv.joinGroupRequest(userId, groupId);
    }

    @PutMapping("/leaveGroup")
    public void leaveGroup() throws Exception {
        var userId = usrSrv.getAuthenticatedUserId();
        usrSrv.leaveGroup(userId);
    }

    @PutMapping("/excludeFromGroup/{userId}")
    public void excludeFromGroup(@PathVariable Integer userId) throws Exception {
        usrSrv.leaveGroup(userId);
    }
}
