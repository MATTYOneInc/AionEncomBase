package com.aionemu.commons.configuration.transformers;

import com.aionemu.commons.configuration.PropertyTransformer;
import com.aionemu.commons.configuration.TransformationException;
import java.lang.reflect.Field;

public class ClassTransformer implements PropertyTransformer<Class<?>> {
   public static final ClassTransformer SHARED_INSTANCE = new ClassTransformer();

   public Class<?> transform(String value, Field field) throws TransformationException {
      try {
         return Class.forName(value, false, this.getClass().getClassLoader());
      } catch (ClassNotFoundException var4) {
         throw new TransformationException("Cannot find class with name '" + value + "'");
      }
   }
}
