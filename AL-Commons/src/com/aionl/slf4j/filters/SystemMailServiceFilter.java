package com.aionl.slf4j.filters;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class SystemMailServiceFilter extends Filter<ILoggingEvent> {
   public FilterReply decide(ILoggingEvent loggingEvent) {
      Object message = loggingEvent.getMessage();
      return ((String)message).startsWith("[SYSMAILSERVICE]") ? FilterReply.ACCEPT : FilterReply.DENY;
   }
}
