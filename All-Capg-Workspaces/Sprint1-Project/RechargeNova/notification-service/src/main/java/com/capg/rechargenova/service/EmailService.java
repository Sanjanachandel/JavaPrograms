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

        helper.setFrom("rechargenova@gmail.com");
        helper.setTo(request.getRecipientEmail());
        helper.setSubject("Recharge Successful - RechargeNova");

        String htmlContent = "<!DOCTYPE html>" +
            "<html>" +
            "<head>" +
            "<style>" +
            "  body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f7fe; margin: 0; padding: 0; }" +
            "  .container { max-width: 600px; margin: 20px auto; background: #ffffff; border-radius: 20px; box-shadow: 0 10px 30px rgba(0,0,0,0.05); overflow: hidden; }" +
            "  .header { background: linear-gradient(135deg, #6366f1 0%, #a855f7 100%); color: white; padding: 40px 20px; text-align: center; }" +
            "  .header h1 { margin: 0; font-size: 28px; font-weight: 700; border-bottom: 2px solid rgba(255,255,255,0.2); padding-bottom: 15px; }" +
            "  .content { padding: 40px 30px; color: #1e293b; line-height: 1.6; }" +
            "  .success-badge { display: inline-block; background: #dcfce7; color: #15803d; padding: 8px 20px; border-radius: 50px; font-weight: 600; margin-bottom: 20px; font-size: 14px; }" +
            "  .details-card { background: #f8fafc; border: 1px solid #e2e8f0; border-radius: 15px; padding: 25px; margin: 20px 0; }" +
            "  .detail-row { display: flex; justify-content: space-between; padding: 12px 0; border-bottom: 1px solid #edf2f7; }" +
            "  .detail-row:last-child { border-bottom: none; }" +
            "  .label { color: #64748b; font-weight: 500; text-align: left; width: 40%; }" +
            "  .value { color: #0f172a; font-weight: 600; text-align: right; width: 60%; }" +
            "  .amount-section { text-align: center; padding: 20px 0; border-top: 2px dashed #e2e8f0; margin-top: 10px; }" +
            "  .amount-value { font-size: 32px; font-weight: 800; color: #6366f1; }" +
            "  .footer { background: #f1f5f9; padding: 30px; text-align: center; color: #64748b; font-size: 12px; }" +
            "  .btn { display: inline-block; padding: 14px 30px; background: #6366f1; color: white !important; text-decoration: none; border-radius: 12px; font-weight: 600; margin-top: 20px; transition: 0.3s; }" +
            "</style>" +
            "</head>" +
            "<body>" +
            "  <div class='container'>" +
            "    <div class='header'>" +
            "      <h1>RechargeNova</h1>" +
            "      <p>Instant Recharge. Infinite Happiness.</p>" +
            "    </div>" +
            "    <div class='content'>" +
            "      <div class='success-badge'>✓ Payment Success</div>" +
            "      <h2>Hello, " + request.getUserName() + "!</h2>" +
            "      <p>Great news! Your mobile recharge has been successfully completed. Here are the details of your transaction:</p>" +
            "      <div class='details-card'>" +
            "        <div class='detail-row'><div class='label'>Mobile Number</div><div class='value'>+91 " + request.getMobileNumber() + "</div></div>" +
            "        <div class='detail-row'><div class='label'>Operator</div><div class='value'>" + request.getOperatorName() + "</div></div>" +
            "        <div class='detail-row'><div class='label'>Validity</div><div class='value'>" + request.getValidity() + "</div></div>" +
            "        <div class='detail-row'><div class='label'>Transaction ID</div><div class='value' style='font-family: monospace;'>" + request.getTransactionId() + "</div></div>" +
            "        <div class='amount-section'>" +
            "          <div class='label' style='margin-bottom: 5px;'>Total Amount Paid</div>" +
            "          <div class='amount-value'>₹" + request.getAmount() + "</div>" +
            "        </div>" +
            "      </div>" +
            "      <p style='text-align: center;'>Thank you for choosing RechargeNova for your mobile services!</p>" +
            "    </div>" +
            "    <div class='footer'>" +
            "      <p>&copy; 2026 RechargeNova Services Pvt Ltd.</p>" +
            "      <p>If you have any questions, contact us at sanjanachandel82@gmail.com</p>" +
            "    </div>" +
            "  </div>" +
            "</body>" +
            "</html>";

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
}
