package com.aionemu.commons.configuration.transformers;

import com.aionemu.commons.configuration.PropertyTransformer;
import com.aionemu.commons.configuration.TransformationException;
import java.lang.reflect.Field;

public class StringTransformer implements PropertyTransformer<String> {
   public static final StringTransformer SHARED_INSTANCE = new StringTransformer();

   public String transform(String value, Field field) throws TransformationException {
      return value;
   }
}
