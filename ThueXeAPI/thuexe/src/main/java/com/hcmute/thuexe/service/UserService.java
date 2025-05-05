package com.hcmute.thuexe.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public String getProfileByUsername(String username) {
        Optional<Account> accountOpt = accountRepo.findByUsername(username);
        if (accountOpt.isEmpty()) return "Không tìm thấy tài khoản";
    
        Optional<User> userOpt = userRepo.findAll().stream()
                .filter(u -> u.getAccount().getUsername().equals(username))
                .findFirst();
    
        if (userOpt.isEmpty()) return "Không tìm thấy thông tin người dùng";
    
        User user = userOpt.get();
        return "Tên: " + user.getName() + ", Email: " + user.getAccount().getEmail() + ", Role: " + user.getRole();
    }
    
} 
