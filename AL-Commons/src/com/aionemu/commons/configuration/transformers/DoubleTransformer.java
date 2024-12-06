package com.aionemu.commons.configuration.transformers;

import com.aionemu.commons.configuration.PropertyTransformer;
import com.aionemu.commons.configuration.TransformationException;
import java.lang.reflect.Field;

public class DoubleTransformer implements PropertyTransformer<Double> {
   public static final DoubleTransformer SHARED_INSTANCE = new DoubleTransformer();

   public Double transform(String value, Field field) throws TransformationException {
      try {
         return Double.parseDouble(value);
      } catch (Exception var4) {
         throw new TransformationException(var4);
      }
   }
}
