package com.huii.admin.common.annotation;

import java.lang.annotation.*;


@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER, ElementType.METHOD })
public @interface Log {

	String title() default "";

	boolean isSaveRequestData() default true;

	boolean isSaveResponseData() default true;
}
