package com.huii.admin.common.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER, ElementType.METHOD })
public @interface LoginLog {

	String value() default "";

	boolean isLoginSuccess() default true;

}
