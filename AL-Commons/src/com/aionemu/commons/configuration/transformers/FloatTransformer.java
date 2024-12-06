package com.aionemu.commons.configuration.transformers;

import com.aionemu.commons.configuration.PropertyTransformer;
import com.aionemu.commons.configuration.TransformationException;
import java.lang.reflect.Field;

public class FloatTransformer implements PropertyTransformer<Float> {
   public static final FloatTransformer SHARED_INSTANCE = new FloatTransformer();

   public Float transform(String value, Field field) throws TransformationException {
      try {
         return Float.parseFloat(value);
      } catch (Exception var4) {
         throw new TransformationException(var4);
      }
   }
}
