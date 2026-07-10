package com.kuugae.ca.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Slf4j
@Configuration
public class MailConfig {

    @Value("${app.mail.enabled:false}")
    private boolean mailEnabled;

    @Value("${app.mail.smtp-host:smtp.qq.com}")
    private String smtpHost;

    @Value("${app.mail.smtp-port:587}")
    private int smtpPort;

    @Value("${app.mail.username:}")
    private String username;

    @Value("${app.mail.password:}")
    private String password;

    @Value("${app.mail.from:noreply@casystem.com}")
    private String mailFrom;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(smtpHost);
        mailSender.setPort(smtpPort);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        mailSender.setDefaultEncoding("UTF-8");
        mailSender.setProtocol("smtp");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.connectiontimeout", "5000");
        props.put("mail.smtp.timeout", "5000");
        props.put("mail.smtp.writetimeout", "5000");

        // 如果未配置用户名密码，禁用认证（用于测试）
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            props.put("mail.smtp.auth", "false");
            log.warn("邮件服务未配置用户名密码，将使用匿名方式发送（可能失败）");
        }

        // 如果邮件服务未启用，设置一个无效的 host 避免实际发送
        if (!mailEnabled) {
            mailSender.setHost("localhost");
            mailSender.setPort(25);
            log.info("邮件服务未启用，JavaMailSender 已配置为本地模式");
        }

        return mailSender;
    }
}
