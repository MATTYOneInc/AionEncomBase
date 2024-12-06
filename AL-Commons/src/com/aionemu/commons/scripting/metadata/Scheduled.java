package com.aionemu.commons.scripting.metadata;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Scheduled {
   String[] value();

   boolean instancePerCronExpression() default false;

   boolean disabled() default false;

   boolean longRunningTask() default false;
}
