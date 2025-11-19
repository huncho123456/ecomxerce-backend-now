package com.ecommerce.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private Long id;
    private String email;
    private String fullName;
    private String role;
    private boolean active;
    private String phoneNumber;
    private String password;
    private String token;

    private String message;

    private String profilePicUrl;

    private UserDto user;
    private java.util.List<UserDto> users;

}
