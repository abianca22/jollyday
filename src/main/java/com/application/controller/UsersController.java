package com.application.controller;

import com.application.exceptions.UserNotFoundException;
import com.application.model.User;
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
        String json = String.format("{\"username\": \"%s\", \"lastName\": \"%s\", \"firstName\": \"%s\", \"email\": \"%s\", \"birthday\": \"%s\", \"groupName\": \"%s\"}",
                usr.getUsername(),
                usr.getLastName(),
                usr.getFirstName(),
                usr.getEmail(),
                bday,
                grp);
        return json;
    }

    @GetMapping("/{username}")
    public String getGivenUser(@PathVariable String username) throws UserNotFoundException {
        User usr = usrSrv.findUserByUsername(username).orElseThrow(UserNotFoundException::new);
        System.out.println(usr);
        return userToJson(usr);
    }

    @GetMapping("/all")
    public List<User> getAllExistingUsers() {

        try {
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
}
