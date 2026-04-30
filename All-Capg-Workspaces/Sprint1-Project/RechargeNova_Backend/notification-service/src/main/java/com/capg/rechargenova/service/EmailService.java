package com.capg.rechargenova.service;

import com.capg.rechargenova.dto.EmailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendRechargeSuccessEmail(EmailRequest request) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("rechargenovanotifications@gmail.com");
        helper.setTo(request.getRecipientEmail());
        helper.setSubject("Recharge Successful - RechargeNova");

        String htmlContent = "<!DOCTYPE html>" +
            "<html>" +
            "<body style=\"font-family: Arial, sans-serif; background-color: #f4f7fe; margin: 0; padding: 20px;\">" +
            "  <div style=\"max-width: 600px; margin: 0 auto; background: #ffffff; border-radius: 20px; overflow: hidden; box-shadow: 0 10px 30px rgba(0,0,0,0.05);\">" +
            "    <div style=\"background: #6366f1; color: white; padding: 40px 20px; text-align: center;\">" +
            "      <h1 style=\"margin: 0; font-size: 28px; font-weight: bold; border-bottom: 2px solid rgba(255,255,255,0.2); padding-bottom: 15px;\">RechargeNova</h1>" +
            "      <p style=\"margin-top: 15px; font-size: 16px;\">Instant Recharge. Infinite Happiness.</p>" +
            "    </div>" +
            "    <div style=\"padding: 40px 30px; color: #1e293b; line-height: 1.6;\">" +
            "      <div style=\"display: inline-block; background: #dcfce7; color: #15803d; padding: 8px 20px; border-radius: 50px; font-weight: bold; margin-bottom: 20px; font-size: 14px;\">✓ Payment Success</div>" +
            "      <h2 style=\"color: #0f172a; font-size: 24px; margin-top: 0;\">Hello, " + request.getUserName() + "!</h2>" +
            "      <p>Great news! Your mobile recharge has been successfully completed. Here are the details of your transaction:</p>" +
            "      <div style=\"background: #f8fafc; border: 1px solid #e2e8f0; border-radius: 15px; padding: 25px; margin: 20px 0;\">" +
            "        <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse: collapse;\">" +
            "          <tr>" +
            "            <td style=\"padding: 12px 0; border-bottom: 1px solid #edf2f7; color: #64748b; font-weight: 500; width: 40%;\">Mobile Number</td>" +
            "            <td style=\"padding: 12px 0; border-bottom: 1px solid #edf2f7; color: #0f172a; font-weight: bold; text-align: right;\">+91 " + request.getMobileNumber() + "</td>" +
            "          </tr>" +
            "          <tr>" +
            "            <td style=\"padding: 12px 0; border-bottom: 1px solid #edf2f7; color: #64748b; font-weight: 500;\">Operator</td>" +
            "            <td style=\"padding: 12px 0; border-bottom: 1px solid #edf2f7; color: #0f172a; font-weight: bold; text-align: right;\">" + request.getOperatorName() + "</td>" +
            "          </tr>" +
            "          <tr>" +
            "            <td style=\"padding: 12px 0; border-bottom: 1px solid #edf2f7; color: #64748b; font-weight: 500;\">Recharge Type</td>" +
            "            <td style=\"padding: 12px 0; border-bottom: 1px solid #edf2f7; color: #0f172a; font-weight: bold; text-align: right;\">" + request.getRechargeType() + "</td>" +
            "          </tr>" +
            "          <tr>" +
            "            <td style=\"padding: 12px 0; color: #64748b; font-weight: 500;\">Validity</td>" +
            "            <td style=\"padding: 12px 0; color: #0f172a; font-weight: bold; text-align: right;\">" + request.getValidity() + " Days</td>" +
            "          </tr>" +
            "        </table>" +
            "        <div style=\"text-align: center; padding: 20px 0 0 0; margin-top: 20px; border-top: 2px dashed #e2e8f0;\">" +
            "          <div style=\"color: #64748b; font-weight: 500; margin-bottom: 5px;\">Total Amount Paid</div>" +
            "          <div style=\"font-size: 32px; font-weight: bold; color: #6366f1;\">₹" + request.getAmount() + "</div>" +
            "        </div>" +
            "      </div>" +
            "      <p style=\"text-align: center;\">Thank you for choosing RechargeNova for your mobile services!</p>" +
            "    </div>" +
            "    <div style=\"background: #f1f5f9; padding: 30px; text-align: center; color: #64748b; font-size: 12px;\">" +
            "      <p>&copy; 2026 RechargeNova Services Pvt Ltd.</p>" +
            "      <p>If you have any questions, contact us at <a href=\"mailto:rechargenovanotifications@gmail.com\" style=\"color: #6366f1;\">rechargenovanotifications@gmail.com</a></p>" +
            "    </div>" +
            "  </div>" +
            "</body>" +
            "</html>";

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

    public void sendOtpEmail(String recipientEmail, String otp) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("rechargenova@gmail.com");
        helper.setTo(recipientEmail);
        helper.setSubject("Password Reset OTP - RechargeNova");

        String htmlContent = "<!DOCTYPE html><html>" +
            "<body style='font-family: Arial, sans-serif; background-color: #f4f7fe; padding: 20px;'>" +
            "  <div style='max-width: 500px; margin: auto; background: white; padding: 30px; border-radius: 15px; box-shadow: 0 4px 12px rgba(0,0,0,0.1);'>" +
            "    <h2 style='color: #6366f1;'>RechargeNova</h2>" +
            "    <p>Hello,</p>" +
            "    <p>You requested a password reset. Use the following 6-digit OTP to complete the process:</p>" +
            "    <div style='background: #f1f5f9; padding: 20px; text-align: center; font-size: 28px; font-weight: 800; color: #6366f1; letter-spacing: 5px; border-radius: 10px; margin: 20px 0;'>" +
                 otp +
            "    </div>" +
            "    <p style='color: #64748b; font-size: 14px;'>This OTP is valid for a limited time. If you didn't request this, please ignore this email.</p>" +
            "    <hr style='border: none; border-top: 1px solid #e2e8f0; margin: 20px 0;'>" +
            "    <p style='font-size: 12px; color: #94a3b8;'>© 2026 RechargeNova. All rights reserved.</p>" +
            "  </div>" +
            "</body></html>";

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
}
