package com.bluevelvet.DTO;

import java.util.List;

public record AdminRegisterDTO(String name, String lastName, String email, String password, List<Integer> roles, Boolean status, byte[] image){
}
