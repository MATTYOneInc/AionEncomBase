package com.aionemu.commons.configuration;

import java.lang.reflect.Field;

public interface PropertyTransformer<T> {
   T transform(String var1, Field var2) throws TransformationException;
}
