package com.aionemu.commons.configuration.transformers;

import com.aionemu.commons.configuration.PropertyTransformer;
import com.aionemu.commons.configuration.TransformationException;
import java.lang.reflect.Field;

public class ShortTransformer implements PropertyTransformer<Short> {
   public static final ShortTransformer SHARED_INSTANCE = new ShortTransformer();

   public Short transform(String value, Field field) throws TransformationException {
      try {
         return Short.decode(value);
      } catch (Exception var4) {
         throw new TransformationException(var4);
      }
   }
}
