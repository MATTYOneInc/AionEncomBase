package com.aionemu.commons.configuration.transformers;

import com.aionemu.commons.configuration.PropertyTransformer;
import com.aionemu.commons.configuration.TransformationException;
import java.lang.reflect.Field;

public class ByteTransformer implements PropertyTransformer<Byte> {
   public static final ByteTransformer SHARED_INSTANCE = new ByteTransformer();

   public Byte transform(String value, Field field) throws TransformationException {
      try {
         return Byte.decode(value);
      } catch (Exception var4) {
         throw new TransformationException(var4);
      }
   }
}
