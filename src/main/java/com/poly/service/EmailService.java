package com.poly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("thhung3107@gmail.com"); // 👈 Đổi thành email của bạn
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true); // `true` để gửi email HTML

            mailSender.send(message);
            System.out.println("Email đã gửi thành công đến: " + to);
        } catch (MessagingException e) {
            System.err.println("Lỗi khi gửi email: " + e.getMessage());
            throw new RuntimeException("Gửi email thất bại", e);
        }
    }
}
