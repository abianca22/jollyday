package com.application.security.config;

import com.application.auth.AuthRes;
import com.application.auth.SignInReq;
import com.application.auth.SignUpReq;
import com.application.model.JoinStatus;
import com.application.model.Role;
import com.application.model.User;
import com.application.repository.UserRepository;
import com.application.security.config.JwtService;
import com.application.utils.UtilMethods;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder pwEnc;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    public AuthRes signup(SignUpReq req) {
        var user = User.builder()
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .email(req.getEmail())
                .username(req.getUsername())
                .password(pwEnc.encode(req.getPassword()))
                .userRole(Role.USER)
                .birthday(UtilMethods.transformDate(req.getBirthday()))
                .joinDate(LocalDate.now())
                .joinStatus(JoinStatus.NONE)
                .build();
        userRepo.save(user);
        var jwtToken = jwtService.generateJwtToken(user);

        return AuthRes.builder()
                .jwtToken(jwtToken)
                .build();
    }

    public AuthRes signin(SignInReq req) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            req.getUsername(),
                            req.getPassword()
                    )
            );
            var user = userRepo.findByUsername(req.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
            var jwtToken = jwtService.generateJwtToken(user);
            return AuthRes.builder()
                    .jwtToken(jwtToken)
                    .build();
        } catch (BadCredentialsException e) {
            return AuthRes.builder().jwtToken(null).build();
        }
    }

}
