package com.aionemu.commons.configuration.transformers;

import com.aionemu.commons.configuration.PropertyTransformer;
import com.aionemu.commons.configuration.TransformationException;
import java.lang.reflect.Field;

public class EnumTransformer implements PropertyTransformer<Enum<?>> {
   public static final EnumTransformer SHARED_INSTANCE = new EnumTransformer();

   public Enum<?> transform(String value, Field field) throws TransformationException {
      Class clazz = field.getType();

      try {
         return Enum.valueOf(clazz, value);
      } catch (Exception var5) {
         throw new TransformationException(var5);
      }
   }
}
