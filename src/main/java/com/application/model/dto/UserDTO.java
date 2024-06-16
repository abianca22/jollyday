package com.application.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class UserDTO {

    private String username;
    private String lastName;
    private String firstName;
    private String email;
    private LocalDate birthday;
    private Integer groupId;

}
