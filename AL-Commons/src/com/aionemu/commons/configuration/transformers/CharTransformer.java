package com.aionemu.commons.configuration.transformers;

import com.aionemu.commons.configuration.PropertyTransformer;
import com.aionemu.commons.configuration.TransformationException;
import java.lang.reflect.Field;

public class CharTransformer implements PropertyTransformer<Character> {
   public static final CharTransformer SHARED_INSTANCE = new CharTransformer();

   public Character transform(String value, Field field) throws TransformationException {
      try {
         char[] chars = value.toCharArray();
         if (chars.length > 1) {
            throw new TransformationException("To many characters in the value");
         } else {
            return chars[0];
         }
      } catch (Exception var4) {
         throw new TransformationException(var4);
      }
   }
}
