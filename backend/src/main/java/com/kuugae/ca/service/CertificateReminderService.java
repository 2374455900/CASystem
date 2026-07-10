package com.kuugae.ca.service;

import com.kuugae.ca.entity.Certificate;
import com.kuugae.ca.entity.User;
import com.kuugae.ca.repository.CertificateRepository;
import com.kuugae.ca.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CertificateReminderService {

    private final CertificateRepository certificateRepository;
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    @Value("${app.mail.enabled:false}")
    private boolean mailEnabled;

    @Value("${app.mail.from:noreply@casystem.com}")
    private String mailFrom;

    @Value("${app.reminder.days-before-expiry:30,15,7}")
    private String reminderDaysConfig;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy 年 MM 月 dd 日 HH:mm:ss");

    @Scheduled(cron = "0 0 9 * * ?")
    public void checkExpiringCertificates() {
        log.info("开始检查即将到期的证书");

        List<Integer> reminderDays = parseReminderDays();
        LocalDate now = LocalDate.now();

        for (Integer days : reminderDays) {
            LocalDate targetDate = now.plusDays(days);
            List<Certificate> expiringCerts = findCertificatesExpiringOnDate(targetDate);

            for (Certificate cert : expiringCerts) {
                if ("使用中".equals(cert.getState())) {
                    sendReminderEmail(cert, days);
                }
            }
        }

        log.info("证书到期检查完成");
    }

    private List<Integer> parseReminderDays() {
        try {
            String[] daysArray = reminderDaysConfig.split(",");
            List<Integer> days = new java.util.ArrayList<>();
            for (String dayStr : daysArray) {
                days.add(Integer.parseInt(dayStr.trim()));
            }
            return days;
        } catch (Exception e) {
            log.error("解析提醒天数配置失败", e);
            return List.of(30, 15, 7);
        }
    }

    private List<Certificate> findCertificatesExpiringOnDate(LocalDate targetDate) {
        LocalDateTime startOfDay = targetDate.atStartOfDay();
        LocalDateTime endOfDay = targetDate.atTime(23, 59, 59);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy 年 MM 月 dd 日 HH:mm:ss");
        String startStr = startOfDay.format(formatter);
        String endStr = endOfDay.format(formatter);

        List<Certificate> allCerts = certificateRepository.findAll();
        return allCerts.stream()
            .filter(cert -> {
                try {
                    LocalDateTime endTime = LocalDateTime.parse(cert.getEndTime(), 
                        DateTimeFormatter.ofPattern("yyyy 年 MM 月 dd 日 HH:mm:ss"));
                    return !endTime.isBefore(startOfDay) && !endTime.isAfter(endOfDay);
                } catch (Exception e) {
                    return false;
                }
            })
            .toList();
    }

    private void sendReminderEmail(Certificate certificate, int daysLeft) {
        if (!mailEnabled) {
            log.info("邮件服务未启用，跳过发送提醒邮件");
            return;
        }

        try {
            User user = userRepository.findByEmail(certificate.getUsername())
                .orElse(null);

            if (user == null) {
                log.warn("未找到证书所属用户：{}", certificate.getUsername());
                return;
            }

            String toEmail = user.getEmail();
            String subject = String.format("【CaSystem】证书到期提醒 - 还有 %d 天到期", daysLeft);
            String content = buildEmailContent(certificate, daysLeft);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(mailFrom);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
            log.info("发送证书到期提醒邮件成功：{} -> {} ({}天后到期)", 
                certificate.getSerialNumber(), toEmail, daysLeft);

        } catch (MessagingException e) {
            log.error("发送证书到期提醒邮件失败：{}", certificate.getSerialNumber(), e);
        }
    }

    private String buildEmailContent(Certificate certificate, int daysLeft) {
        StringBuilder content = new StringBuilder();
        content.append("<html><body>");
        content.append("<h2 style='color: #333;'>证书到期提醒</h2>");
        content.append("<p>尊敬的用户：</p>");
        content.append("<p>您名下的以下数字证书即将到期，请及时处理。</p>");
        
        content.append("<table style='border-collapse: collapse; width: 100%; margin: 20px 0;'>");
        content.append("<tr style='background-color: #f2f2f2;'>");
        content.append("<td style='border: 1px solid #ddd; padding: 10px;'><strong>证书序列号</strong></td>");
        content.append("<td style='border: 1px solid #ddd; padding: 10px;'>").append(certificate.getSerialNumber()).append("</td>");
        content.append("</tr>");
        
        content.append("<tr>");
        content.append("<td style='border: 1px solid #ddd; padding: 10px;'><strong>组织名称</strong></td>");
        content.append("<td style='border: 1px solid #ddd; padding: 10px;'>").append(certificate.getOrganization()).append("</td>");
        content.append("</tr>");
        
        content.append("<tr style='background-color: #f2f2f2;'>");
        content.append("<td style='border: 1px solid #ddd; padding: 10px;'><strong>有效期</strong></td>");
        content.append("<td style='border: 1px solid #ddd; padding: 10px;'>")
            .append(certificate.getStartTime()).append(" 至 ").append(certificate.getEndTime())
            .append("</td>");
        content.append("</tr>");
        
        content.append("<tr>");
        content.append("<td style='border: 1px solid #ddd; padding: 10px;'><strong>剩余天数</strong></td>");
        content.append("<td style='border: 1px solid #ddd; padding: 10px;'>");
        
        if (daysLeft <= 7) {
            content.append("<span style='color: red; font-weight: bold;'>").append(daysLeft).append(" 天</span>");
        } else if (daysLeft <= 15) {
            content.append("<span style='color: orange; font-weight: bold;'>").append(daysLeft).append(" 天</span>");
        } else {
            content.append("<span style='color: #333;'>").append(daysLeft).append(" 天</span>");
        }
        
        content.append("</td>");
        content.append("</tr>");
        content.append("</table>");
        
        content.append("<p style='color: #666;'><strong>温馨提示：</strong></p>");
        content.append("<ul style='color: #666;'>");
        content.append("<li>证书到期后将无法正常使用，请提前申请续期或重新申请</li>");
        content.append("<li>请登录 CaSystem 系统查看证书详情</li>");
        content.append("<li>如有问题，请联系系统管理员</li>");
        content.append("</ul>");
        
        content.append("<hr style='border: none; border-top: 1px solid #ddd; margin: 20px 0;'>");
        content.append("<p style='color: #999; font-size: 12px;'>此邮件由 CaSystem 系统自动发送，请勿回复。</p>");
        content.append("</body></html>");

        return content.toString();
    }

    public void sendManualReminderEmail(String serialNumber) {
        Certificate certificate = certificateRepository.findBySerialNumber(serialNumber)
            .orElseThrow(() -> new RuntimeException("证书不存在"));

        int daysLeft = calculateDaysLeft(certificate.getEndTime());
        sendReminderEmail(certificate, daysLeft);
    }

    private int calculateDaysLeft(String endTimeStr) {
        try {
            LocalDateTime endTime = LocalDateTime.parse(endTimeStr, 
                DateTimeFormatter.ofPattern("yyyy 年 MM 月 dd 日 HH:mm:ss"));
            return (int) java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), endTime.toLocalDate());
        } catch (Exception e) {
            return -1;
        }
    }
}
