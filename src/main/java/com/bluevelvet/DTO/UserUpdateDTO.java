package com.bluevelvet.DTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDTO {

    @NotEmpty(message = "Name is required")
    private String name;

    @NotEmpty(message = "Last name is required")
    private String lastName;

    @NotEmpty(message = "Email is required")
    private String email;

    private String password;

    private Boolean status;
}