package com.example.DAuction.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String fullName;
    private String email;
    private String phoneNumber;
    private String country;
    private String state;
    private String LGA;
}
