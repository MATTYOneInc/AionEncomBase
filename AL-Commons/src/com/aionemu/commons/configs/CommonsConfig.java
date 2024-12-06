package com.aionemu.commons.configs;

import com.aionemu.commons.configuration.Property;

public class CommonsConfig {
   @Property(
      key = "commons.runnablestats.enable",
      defaultValue = "false"
   )
   public static boolean RUNNABLESTATS_ENABLE;
}
