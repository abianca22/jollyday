package com.application.auth;

import com.application.exceptions.UserNotFoundException;
import com.application.model.Role;
import com.application.model.User;
import com.application.service.UserService;
import com.application.security.config.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/jollyday/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4300")
public class AuthenticationController {

    private final AuthService authService;

    private final JwtTokenBlacklist jwtList;

    private final UserService usrSrv;

    private String lastTkn = "";

    private Role lastUserRole = null;

    private String lastUsername = null;
    @PostMapping("/signup")
    public ResponseEntity<AuthRes> signup (
            @RequestBody SignUpReq req
    ) {
        return ResponseEntity.ok(authService.signup(req));
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthRes> signin (
            @RequestBody SignInReq req
    ) throws UserNotFoundException {
        var res = authService.signin(req);
        lastTkn = res.getJwtToken();
        User usr = usrSrv.findUserByUsername(req.getUsername()).orElseThrow(UserNotFoundException::new);
        lastUserRole = usr.getUserRole();
        lastUsername = usr.getUsername();
        return ResponseEntity.ok(res);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestHeader("Authorization") String tkn) {
        if(tkn.startsWith("Bearer ")) {
            tkn = tkn.substring(7);
        }
        jwtList.blackList(tkn);
        SecurityContextHolder.clearContext();
        Map<String, String> logoutmsg = new HashMap<>();
        logoutmsg.put("message", "Deconectarea s-a produs cu succes!");
        lastTkn = "token expirat";
        lastUserRole = null;
        lastUsername = null;
        return ResponseEntity.ok(logoutmsg);
    }

    @GetMapping("/loggedInUser")
    public String getLoggedInUser() {
        String loggedInUsr = lastTkn;
        return "{\"jwtToken\": \"" + lastTkn + "\", \"userRole\": \"" + lastUserRole + "\", \"username\": \"" + lastUsername + "\"}";
    }

}
