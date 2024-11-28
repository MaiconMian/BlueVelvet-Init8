package com.bluevelvet.DTO;

import com.bluevelvet.model.Permissions;
import com.bluevelvet.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoleDTO {

    @NotNull(message = "role name is required")
    private String name;
    private String description;
    private List<Integer> permissions;

}
