package com.aionemu.commons.configuration.transformers;

import com.aionemu.commons.configuration.PropertyTransformer;
import com.aionemu.commons.configuration.TransformationException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class InetSocketAddressTransformer implements PropertyTransformer<InetSocketAddress> {
   public static final InetSocketAddressTransformer SHARED_INSTANCE = new InetSocketAddressTransformer();

   public InetSocketAddress transform(String value, Field field) throws TransformationException {
      String[] parts = value.split(":");
      if (parts.length != 2) {
         throw new TransformationException("Can't transform property, must be in format \"address:port\"");
      } else {
         try {
            if ("*".equals(parts[0])) {
               return new InetSocketAddress(Integer.parseInt(parts[1]));
            } else {
               InetAddress address = InetAddress.getByName(parts[0]);
               int port = Integer.parseInt(parts[1]);
               return new InetSocketAddress(address, port);
            }
         } catch (Exception var6) {
            throw new TransformationException(var6);
         }
      }
   }
}
