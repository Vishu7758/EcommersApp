package com.app.ecom.dto;

import enums.UserRole;
import lombok.Data;

@Data
public class UserResponseDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private UserRole role;
    private AddressDTO address;
}

