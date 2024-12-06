package com.aionemu.commons.configuration.transformers;

import com.aionemu.commons.configuration.PropertyTransformer;
import com.aionemu.commons.configuration.TransformationException;
import java.lang.reflect.Field;

public class LongTransformer implements PropertyTransformer<Long> {
   public static final LongTransformer SHARED_INSTANCE = new LongTransformer();

   public Long transform(String value, Field field) throws TransformationException {
      try {
         return Long.decode(value);
      } catch (Exception var4) {
         throw new TransformationException(var4);
      }
   }
}
