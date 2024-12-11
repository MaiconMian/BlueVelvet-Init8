package com.bluevelvet.DTO;

import java.util.ArrayList;
import java.util.List;

public record UserInformationsDTO(String name, String lastName, String email, byte[] image, List<String> roles){
}