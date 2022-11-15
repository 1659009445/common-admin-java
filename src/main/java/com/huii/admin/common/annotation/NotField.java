package com.huii.admin.common.annotation;

import java.lang.annotation.*;

/**
 * 标记不属于实体类的属性
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotField {
}
