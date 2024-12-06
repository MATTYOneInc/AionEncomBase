package com.aionemu.commons.configuration.transformers;

import com.aionemu.commons.configuration.PropertyTransformer;
import com.aionemu.commons.configuration.TransformationException;
import java.lang.reflect.Field;
import java.util.regex.Pattern;

public class PatternTransformer implements PropertyTransformer<Pattern> {
   public static final PatternTransformer SHARED_INSTANCE = new PatternTransformer();

   public Pattern transform(String value, Field field) throws TransformationException {
      try {
         return Pattern.compile(value);
      } catch (Exception var4) {
         throw new TransformationException("Not valid RegExp: " + value, var4);
      }
   }
}
