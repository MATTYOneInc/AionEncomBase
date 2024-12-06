package com.aionemu.commons.configuration.transformers;

import com.aionemu.commons.configuration.PropertyTransformer;
import com.aionemu.commons.configuration.TransformationException;
import java.io.File;
import java.lang.reflect.Field;

public class FileTransformer implements PropertyTransformer<File> {
   public static final FileTransformer SHARED_INSTANCE = new FileTransformer();

   public File transform(String value, Field field) throws TransformationException {
      return new File(value);
   }
}
