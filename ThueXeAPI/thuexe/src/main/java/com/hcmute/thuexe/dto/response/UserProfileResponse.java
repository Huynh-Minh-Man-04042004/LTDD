package com.hcmute.thuexe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
    private Long userId;
    private String name;
    private String email;
    private String phone;
    private String gender;
    private String birthdate;
    private String imageUrl;
    private String role;
    private String createdAt;
}
