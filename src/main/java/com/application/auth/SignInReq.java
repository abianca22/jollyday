package com.application.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignInReq {
    @NotBlank(message="camp obligatoriu")
    private String username;
    @NotBlank(message="camp obligatoriu")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$", message="Parola trebuie sa aiba 8-20 caractere, litere mari, mici, cifre si caractere speciale!")
    private String password;
}
