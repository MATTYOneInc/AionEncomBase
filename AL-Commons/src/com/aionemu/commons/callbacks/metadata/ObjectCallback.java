package com.aionemu.commons.callbacks.metadata;

import com.aionemu.commons.callbacks.Callback;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ObjectCallback {
   Class<? extends Callback> value();
}
