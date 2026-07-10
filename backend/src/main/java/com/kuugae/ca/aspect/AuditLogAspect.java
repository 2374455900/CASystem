package com.kuugae.ca.aspect;

import com.kuugae.ca.annotation.AuditOperation;
import com.kuugae.ca.service.AuditLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuditLogAspect {

    private final AuditLogService auditLogService;

    @Around("@annotation(com.kuugae.ca.annotation.AuditOperation)")
    public Object logAudit(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        AuditOperation auditOperation = method.getAnnotation(AuditOperation.class);

        String operator = getCurrentUser();
        String operationType = auditOperation.type();
        String targetType = auditOperation.targetType();
        String targetId = getTargetId(joinPoint, auditOperation.targetId());
        String ipAddress = getClientIp();
        String userAgent = getUserAgent();

        long startTime = System.currentTimeMillis();
        String result = "SUCCESS";
        String details = "";

        try {
            Object proceed = joinPoint.proceed();
            details = "执行时间：" + (System.currentTimeMillis() - startTime) + "ms";
            return proceed;
        } catch (Exception e) {
            result = "FAILURE";
            details = "错误：" + e.getMessage();
            throw e;
        } finally {
            auditLogService.logOperation(
                operator,
                operationType,
                targetType,
                targetId,
                ipAddress,
                userAgent,
                result,
                details
            );
        }
    }

    private String getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() != null) {
                return authentication.getName();
            }
        } catch (Exception e) {
            // 忽略异常
        }
        return "anonymous";
    }

    private String getClientIp() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String xfHeader = request.getHeader("X-Forwarded-For");
                if (xfHeader != null && !xfHeader.isEmpty()) {
                    return xfHeader.split(",")[0].trim();
                }
                return request.getRemoteAddr();
            }
        } catch (Exception e) {
            // 忽略异常
        }
        return "unknown";
    }

    private String getUserAgent() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                return request.getHeader("User-Agent");
            }
        } catch (Exception e) {
            // 忽略异常
        }
        return "unknown";
    }

    private String getTargetId(ProceedingJoinPoint joinPoint, String defaultTargetId) {
        if (!defaultTargetId.isEmpty()) {
            return defaultTargetId;
        }

        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof String) {
                String strArg = (String) arg;
                if (strArg.length() > 5 && strArg.length() < 100) {
                    return strArg;
                }
            }
        }

        return "unknown";
    }
}
