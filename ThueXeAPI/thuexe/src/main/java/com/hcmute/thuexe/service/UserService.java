package com.hcmute.thuexe.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.hcmute.thuexe.dto.response.UserProfileResponse;
import com.hcmute.thuexe.dto.response.UserSearchResponse;
import com.hcmute.thuexe.exception.ResourceNotFoundException;
import com.hcmute.thuexe.model.Account;
import com.hcmute.thuexe.model.User;
import com.hcmute.thuexe.repository.AccountRepository;
import com.hcmute.thuexe.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private UserRepository userRepo;

    /**
     * Trả về profile cơ bản của người dùng đang login
     */
    public UserProfileResponse getProfile(Authentication authentication) {
        String username = authentication.getName();
    
        Account account = accountRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tài khoản"));
    
        User user = userRepo.findByAccount_Username(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thông tin người dùng"));
    
        return new UserProfileResponse(
            user.getUserId(),
            user.getName(),
            account.getEmail(),
            user.getPhone(),
            user.getGender(),
            user.getBirthdate() != null ? user.getBirthdate().toString() : null,
            user.getImageUrl(),
            user.getRole(),
            user.getCreatedAt() != null ? user.getCreatedAt().toString() : null
        );
    }    

    /**
     * Tìm user theo từ khóa (name hoặc email), không bao gồm bản thân
     */
    public List<UserSearchResponse> searchUsers(String keyword, Authentication authentication) {
        String username = authentication.getName();
        User currentUser = userRepo.findByAccount_Username(username)
                .orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại"));

        List<User> matchedUsers = userRepo.searchUsersByKeyword(keyword, currentUser.getUserId());

        return matchedUsers.stream()
                .map(u -> new UserSearchResponse(
                        u.getUserId(),
                        u.getName(),
                        u.getAccount().getEmail(),
                        u.getImageUrl()
                ))
                .collect(Collectors.toList());
    }
}
