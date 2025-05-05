package com.hcmute.thuexe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcmute.thuexe.dto.request.ForgotPasswordRequest;
import com.hcmute.thuexe.dto.request.LoginRequest;
import com.hcmute.thuexe.dto.request.RegisterRequest;
import com.hcmute.thuexe.dto.request.ResetPasswordRequest;
import com.hcmute.thuexe.dto.request.VerifyOtpRequest;
import com.hcmute.thuexe.payload.ApiResponse;
import com.hcmute.thuexe.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ApiResponse<String> register(@RequestBody @Valid RegisterRequest request) {
        String message = authService.register(
            request.getUsername(),
            request.getEmail(),
            request.getPassword(),
            request.getConfirmPassword()
        );

        // Trả về success=false nếu có lỗi
        if (message.contains("đã tồn tại") || message.contains("không khớp") ||
            message.contains("đã được sử dụng") || message.contains("chưa xác minh")) {
            return new ApiResponse<>(false, message);
        }

        return new ApiResponse<>(true, message);
    }

    @PostMapping("/verify-otp")
    public ApiResponse<String> verifyOtp(@RequestBody @Valid VerifyOtpRequest request) {
        String message = authService.verifyOtp(request.getEmail(), request.getOtpCode());

        // Kiểm tra lỗi, trả về success=false nếu có vấn đề
        if (message.contains("không hợp lệ") || message.contains("không tồn tại") || message.contains("hết hạn")) {
            return new ApiResponse<>(false, message);
        }

        return new ApiResponse<>(true, message);
    }

    @PostMapping("/login")
    public ApiResponse<String> login(@RequestBody @Valid LoginRequest request) {
        String message = authService.login(request.getUsername(), request.getPassword());

        // Nếu thông báo có lỗi, trả về success=false và lỗi
        if (message.contains("không tồn tại") || 
            message.contains("chưa được kích hoạt") || 
            message.contains("không đúng") ||
            message.contains("Không tìm thấy")) {
            return new ApiResponse<>(false, message);
        }

        // Nếu login thành công, trả về success=true và token
        return new ApiResponse<>(true, "Đăng nhập thành công", message);
    }

    @PostMapping("/forgot-password")
    public ApiResponse<String> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) {
        String message = authService.forgotPassword(request.getEmail());

        // Nếu email không tồn tại trong hệ thống
        if (message.contains("không tồn tại") || message.contains("vừa yêu cầu")) {
            return new ApiResponse<>(false, message);
        }

        return new ApiResponse<>(true, message);
    }

    @PostMapping("/reset-password")
    public ApiResponse<String> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        String message = authService.resetPassword(
            request.getEmail(),
            request.getOtpCode(),
            request.getNewPassword()
        );

        // Kiểm tra nếu OTP không hợp lệ hoặc hết hạn
        if (message.contains("không hợp lệ") || message.contains("hết hạn") || message.contains("không tồn tại")) {
            return new ApiResponse<>(false, message);
        }

        return new ApiResponse<>(true, message);
    }
}
