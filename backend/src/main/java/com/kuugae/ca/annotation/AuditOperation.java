package com.kuugae.ca.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuditOperation {
    String type();
    String targetType() default "";
    String targetId() default "";
}
