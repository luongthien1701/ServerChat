package com.example.demo.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserProfile {
    private Long id;
    private String displayName;
    @JsonIgnore
    private String password;
    private String email;
    private String phone;
}
