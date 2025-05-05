package com.hcmute.thuexe.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcmute.thuexe.model.Otp;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findByEmailAndOtpCodeAndIsUsedFalse(String email, String otpCode);
    void deleteByIsUsedTrueOrExpiresAtBefore(LocalDateTime time);
    Optional<Otp> findTopByEmailOrderByCreatedAtDesc(String email);
}
