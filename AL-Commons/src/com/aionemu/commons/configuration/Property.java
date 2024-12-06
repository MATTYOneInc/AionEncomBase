package com.aionemu.commons.configuration;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Property {
   String DEFAULT_VALUE = "DO_NOT_OVERWRITE_INITIALIAZION_VALUE";

   String key();

   Class<? extends PropertyTransformer> propertyTransformer() default PropertyTransformer.class;

   String defaultValue() default "DO_NOT_OVERWRITE_INITIALIAZION_VALUE";
}
