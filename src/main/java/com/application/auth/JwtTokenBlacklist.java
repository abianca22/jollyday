package com.application.auth;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class JwtTokenBlacklist {
    private Set<String> jwtBlacklist = new HashSet<>();

    public void blackList(String jwtToken) {
        this.jwtBlacklist.add(jwtToken);
    }

    public boolean alreadyInBlacklist(String jwtToken) {
        return this.jwtBlacklist.contains(jwtToken);
    }
}
