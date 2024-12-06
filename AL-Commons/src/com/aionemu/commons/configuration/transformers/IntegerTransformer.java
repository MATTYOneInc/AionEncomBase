package com.aionemu.commons.configuration.transformers;

import com.aionemu.commons.configuration.PropertyTransformer;
import com.aionemu.commons.configuration.TransformationException;
import java.lang.reflect.Field;

public class IntegerTransformer implements PropertyTransformer<Integer> {
   public static final IntegerTransformer SHARED_INSTANCE = new IntegerTransformer();

   public Integer transform(String value, Field field) throws TransformationException {
      try {
         return Integer.decode(value);
      } catch (Exception var4) {
         throw new TransformationException(var4);
      }
   }
}
