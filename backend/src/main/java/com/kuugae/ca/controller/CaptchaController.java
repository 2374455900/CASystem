package com.kuugae.ca.controller;

import com.kuugae.ca.dto.ApiResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Slf4j
@RestController
@RequestMapping("/api/captcha")
@RequiredArgsConstructor
public class CaptchaController {

    private static final String CAPTCHA_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int WIDTH = 120;
    private static final int HEIGHT = 40;
    private static final int CODE_LENGTH = 4;
    private static final String SESSION_KEY = "checkCode_session";

    private final HttpSession httpSession;

    @GetMapping("/generate")
    public ResponseEntity<ApiResponse<Map<String, Object>>> generateCaptcha() throws IOException {
        String captchaCode = generateCode();
        BufferedImage image = generateImage(captchaCode);

        // 将验证码存储到 Session
        if (httpSession != null) {
            httpSession.setAttribute(SESSION_KEY, captchaCode);
            log.debug("验证码已存储到 Session: {}", httpSession.getId());
        } else {
            log.warn("HttpSession 为 null，无法存储验证码");
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] imageBytes = baos.toByteArray();
        String base64Image = java.util.Base64.getEncoder().encodeToString(imageBytes);

        Map<String, Object> data = new HashMap<>();
        data.put("image", "data:image/png;base64," + base64Image);
        data.put("code", captchaCode);

        return ResponseEntity.ok(ApiResponse.success(data));
    }

    private String generateCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CAPTCHA_CHARS.charAt(random.nextInt(CAPTCHA_CHARS.length())));
        }
        return code.toString();
    }

    private BufferedImage generateImage(String code) {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setFont(new Font("Arial", Font.BOLD, 24));
        Random random = new Random();

        for (int i = 0; i < code.length(); i++) {
            g.setColor(new Color(random.nextInt(150), random.nextInt(150), random.nextInt(150)));
            int x = 20 + i * 25;
            int y = 25 + random.nextInt(10);
            g.drawString(String.valueOf(code.charAt(i)), x, y);
        }

        for (int i = 0; i < 5; i++) {
            g.setColor(new Color(random.nextInt(200), random.nextInt(200), random.nextInt(200)));
            int x1 = random.nextInt(WIDTH);
            int y1 = random.nextInt(HEIGHT);
            int x2 = random.nextInt(WIDTH);
            int y2 = random.nextInt(HEIGHT);
            g.drawLine(x1, y1, x2, y2);
        }

        for (int i = 0; i < 50; i++) {
            g.setColor(new Color(random.nextInt(200), random.nextInt(200), random.nextInt(200)));
            int x = random.nextInt(WIDTH);
            int y = random.nextInt(HEIGHT);
            g.drawRect(x, y, 1, 1);
        }

        g.dispose();
        return image;
    }
}
