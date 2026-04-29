package com.poly.controller.asm_java5.service;

import com.poly.controller.asm_java5.entity.User;
import com.poly.controller.asm_java5.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    public String register(String fullName, String email, String password, String confirmPassword, Boolean acceptTerms) {
        if (fullName == null || fullName.isBlank()) {
            return "Họ và tên không được để trống";
        }

        if (email == null || email.isBlank()) {
            return "Email không được để trống";
        }

        if (password == null || password.isBlank()) {
            return "Mật khẩu không được để trống";
        }

        if (!password.equals(confirmPassword)) {
            return "Mật khẩu xác nhận không khớp";
        }

        if (!Boolean.TRUE.equals(acceptTerms)) {
            return "Bạn phải đồng ý với điều khoản dịch vụ và chính sách bảo mật";
        }

        if (userRepository.existsByEmail(email)) {
            return "Email đã tồn tại";
        }

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("customer");

        userRepository.save(user);
        return "Đăng ký thành công";
    }

    public User login(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return null;
        }

        User user = optionalUser.get();
        String storedPassword = user.getPassword();
        boolean matched;

        if (storedPassword != null && storedPassword.startsWith("$2")) {
            matched = passwordEncoder.matches(password, storedPassword);
        } else {
            matched = storedPassword != null && storedPassword.equals(password);
            if (matched) {
                user.setPassword(passwordEncoder.encode(password));
                userRepository.save(user);
            }
        }

        return matched ? user : null;
    }

    public String sendOtpToEmail(String email) {
        if (email == null || email.isBlank()) {
            return "Email không được để trống";
        }

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return "Email không tồn tại trong hệ thống";
        }

        User user = optionalUser.get();
        String otp = String.valueOf((int) (Math.random() * 900000) + 100000);

        user.setOtpCode(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
        userRepository.save(user);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Mã OTP khôi phục mật khẩu");
        message.setText("Xin chào " + user.getFullName() + ",\n\n" +
                "Mã OTP để đặt lại mật khẩu của bạn là: " + otp + "\n" +
                "Mã có hiệu lực trong 5 phút.\n\n" +
                "Vui lòng không chia sẻ mã này cho người khác.");

        mailSender.send(message);
        return "Đã gửi mã OTP về email";
    }

    public String resetPasswordByOtp(String email, String otp, String newPassword, String confirmPassword) {
        if (email == null || email.isBlank()) {
            return "Email không được để trống";
        }

        if (otp == null || otp.isBlank()) {
            return "OTP không được để trống";
        }

        if (newPassword == null || newPassword.isBlank()) {
            return "Mật khẩu mới không được để trống";
        }

        if (!newPassword.equals(confirmPassword)) {
            return "Mật khẩu xác nhận không khớp";
        }

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return "Email không tồn tại trong hệ thống";
        }

        User user = optionalUser.get();

        if (user.getOtpCode() == null || user.getOtpExpiry() == null) {
            return "Bạn chưa yêu cầu mã OTP";
        }

        if (!otp.equals(user.getOtpCode())) {
            return "OTP không đúng";
        }

        if (user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            return "OTP đã hết hạn";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setOtpCode(null);
        user.setOtpExpiry(null);
        userRepository.save(user);
        return "Đổi mật khẩu thành công";
    }
}
