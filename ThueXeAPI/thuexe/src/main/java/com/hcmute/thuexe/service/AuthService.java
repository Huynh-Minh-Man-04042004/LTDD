package com.hcmute.thuexe.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hcmute.thuexe.model.Account;
import com.hcmute.thuexe.model.Otp;
import com.hcmute.thuexe.model.User;
import com.hcmute.thuexe.repository.AccountRepository;
import com.hcmute.thuexe.repository.OtpRepository;
import com.hcmute.thuexe.repository.UserRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class AuthService {

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private OtpRepository otpRepo;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public String register(String username, String email, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            return "Mật khẩu và nhập lại mật khẩu không khớp.";
        }

        Optional<Account> existingByEmail = accountRepo.findByEmail(email);
        if (existingByEmail.isPresent()) {
            Account existingAccount = existingByEmail.get();

            if (existingAccount.isActive()) {
                return "Email đã được sử dụng.";
            }

            if (isOtpRecentlySent(email)) {
                return "Tài khoản đã tồn tại nhưng chưa xác minh. Vui lòng đợi 5 phút để gửi lại mã OTP.";
            }

            Otp otp = new Otp();
            otp.setEmail(email);
            otp.setOtpCode(generateOtp());
            otp.setCreatedAt(LocalDateTime.now());
            otp.setExpiresAt(LocalDateTime.now().plusMinutes(5));
            otp.setUsed(false);
            otpRepo.save(otp);

            sendOtpEmail(email, otp.getOtpCode());

            return "Tài khoản đã tồn tại nhưng chưa xác minh. Đã gửi lại mã OTP.";
        }

        if (accountRepo.existsByUsername(username)) {
            return "Username đã tồn tại.";
        }

        Account account = new Account();
        account.setUsername(username);
        account.setEmail(email);
        account.setPassword(passwordEncoder.encode(password));
        account.setActive(false);
        accountRepo.save(account);

        Otp otp = new Otp();
        otp.setEmail(email);
        otp.setOtpCode(generateOtp());
        otp.setCreatedAt(LocalDateTime.now());
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        otp.setUsed(false);
        otpRepo.save(otp);

        sendOtpEmail(email, otp.getOtpCode());

        return "Đăng ký thành công. Vui lòng kiểm tra email để xác nhận OTP.";
    }

    public String verifyOtp(String email, String code) {
        Optional<Otp> otpOpt = otpRepo.findByEmailAndOtpCodeAndIsUsedFalse(email, code);
        if (otpOpt.isEmpty()) return "OTP không hợp lệ hoặc đã được sử dụng";

        Otp otp = otpOpt.get();
        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) return "OTP đã hết hạn";

        Optional<Account> accountOpt = accountRepo.findByEmail(email);
        if (accountOpt.isEmpty()) return "Tài khoản không tồn tại";

        Account account = accountOpt.get();
        account.setActive(true);
        accountRepo.save(account);

        otp.setUsed(true);
        otpRepo.save(otp);

        User user = new User();
        user.setAccount(account);
        user.setRole("customer");
        userRepo.save(user);

        return "Xác thực thành công. Bạn có thể đăng nhập.";
    }

    public String login(String username, String password) {
        Optional<Account> accountOpt = accountRepo.findByUsername(username);
        if (accountOpt.isEmpty()) return "Tài khoản không tồn tại";

        Account account = accountOpt.get();

        if (!account.isActive()) return "Tài khoản chưa được kích hoạt";

        if (!passwordEncoder.matches(password, account.getPassword())) {
            return "Mật khẩu không đúng";
        }
        
        Optional<User> userOpt = userRepo.findAll().stream()
            .filter(u -> u.getAccount().getUsername().equals(username))
            .findFirst();

        if (userOpt.isEmpty()) return "Không tìm thấy thông tin người dùng";

        String role = userOpt.get().getRole();
        String token = jwtService.generateToken(username, role, userOpt.get().getUserId());

        return token;
    }

    public String forgotPassword(String email) {
        Optional<Account> accountOpt = accountRepo.findByEmail(email);
        if (accountOpt.isEmpty()) return "Email không tồn tại trong hệ thống";

        if (isOtpRecentlySent(email)) {
            return "Bạn vừa yêu cầu OTP. Vui lòng đợi 5 phút để gửi lại.";
        }

        Otp otp = new Otp();
        otp.setEmail(email);
        otp.setOtpCode(generateOtp());
        otp.setCreatedAt(LocalDateTime.now());
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        otp.setUsed(false);
        otpRepo.save(otp);

        sendOtpEmail(email, otp.getOtpCode());

        return "Đã gửi mã OTP để đặt lại mật khẩu. Vui lòng kiểm tra email.";
    }

    public String resetPassword(String email, String otpCode, String newPassword) {
        Optional<Otp> otpOpt = otpRepo.findByEmailAndOtpCodeAndIsUsedFalse(email, otpCode);
        if (otpOpt.isEmpty()) return "OTP không hợp lệ hoặc đã được sử dụng";

        Otp otp = otpOpt.get();
        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) return "OTP đã hết hạn";

        Optional<Account> accountOpt = accountRepo.findByEmail(email);
        if (accountOpt.isEmpty()) return "Tài khoản không tồn tại";

        Account account = accountOpt.get();
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepo.save(account);

        otp.setUsed(true);
        otpRepo.save(otp);

        return "Đặt lại mật khẩu thành công!";
    }

    private boolean isOtpRecentlySent(String email) {
        Optional<Otp> latestOtpOpt = otpRepo.findTopByEmailOrderByCreatedAtDesc(email);
        return latestOtpOpt
                .filter(otp -> !otp.isUsed() && otp.getCreatedAt().isAfter(LocalDateTime.now().minusMinutes(5)))
                .isPresent();
    }

    private String generateOtp() {
        return String.valueOf((int) ((Math.random() * 900000) + 100000)); // 6 số
    }

    private void sendOtpEmail(String toEmail, String otpCode) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
    
            helper.setTo(toEmail);
            helper.setSubject("Mã OTP Xác Thực Tài Khoản");
    
            String htmlContent = "<html><body style='font-family: Arial, sans-serif;'>"
                + "<h2 style='color: #2e6c80;'>Xác thực tài khoản</h2>"
                + "<p>Xin chào,</p>"
                + "<p>Mã OTP của bạn là: <strong style='color: #d9534f; font-size: 20px;'>" + otpCode + "</strong></p>"
                + "<p>Mã OTP có hiệu lực trong vòng <strong>5 phút</strong>.</p>"
                + "<hr>"
                + "<p style='font-size: 12px; color: gray;'>Đây là email tự động, vui lòng không trả lời email này.</p>"
                + "</body></html>";
    
            helper.setText(htmlContent, true); // ⚡ true để mail hiểu đây là HTML
    
            mailSender.send(message);
        } catch (MessagingException | MailException e) {
            e.printStackTrace();
        }
    }
    
}
