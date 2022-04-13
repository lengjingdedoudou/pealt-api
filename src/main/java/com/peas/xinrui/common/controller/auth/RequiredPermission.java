package com.peas.xinrui.common.controller.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.peas.xinrui.api.admin.entity.AdminPermission;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiredPermission {
    AdminPermission[] value() default { AdminPermission.NONE };

}