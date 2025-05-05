package com.hcmute.thuexe.service;

import com.hcmute.thuexe.repository.OtpRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OtpCleanupService {

    private final OtpRepository otpRepository;

    public OtpCleanupService(OtpRepository otpRepository) {
        this.otpRepository = otpRepository;
    }

    // Chạy mỗi 5 phút
    @Scheduled(fixedRate = 5 * 60 * 1000)
    @Transactional
    public void cleanUpExpiredOtps() {
        otpRepository.deleteByIsUsedTrueOrExpiresAtBefore(LocalDateTime.now());
    }
}
