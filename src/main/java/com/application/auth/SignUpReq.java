package com.application.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpReq {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String username;
    private String birthday;
}
