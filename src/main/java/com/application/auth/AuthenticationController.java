package com.application.auth;

import com.application.exceptions.UserNotFoundException;
import com.application.service.UserService;
import com.application.security.config.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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

    @PostMapping("/signup")
    public ResponseEntity<AuthRes> signup (
            @RequestBody SignUpReq req,
            HttpServletResponse response
    ) {
        var res = authService.signup(req);
        Cookie cookie = new Cookie("jwt", res.getJwtToken());
        cookie.setMaxAge(24 * 60 * 60);
        response.addCookie(cookie);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthRes> signin (
            @RequestBody SignInReq req,
            HttpServletResponse response
    ) throws UserNotFoundException {
        var res = authService.signin(req);
        Cookie cookie = new Cookie("jwt", res.getJwtToken());
        cookie.setMaxAge(24 * 60 * 60);
        response.addCookie(cookie);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestHeader("Authorization") String tkn, HttpServletResponse response) {
        if(tkn.startsWith("Bearer ")) {
            tkn = tkn.substring(7);
        }
        jwtList.blackList(tkn);
        SecurityContextHolder.clearContext();
        Map<String, String> logoutmsg = new HashMap<>();
        logoutmsg.put("message", "Deconectarea s-a produs cu succes!");
        Cookie cookie = new Cookie("jwt", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok(logoutmsg);
    }



}
