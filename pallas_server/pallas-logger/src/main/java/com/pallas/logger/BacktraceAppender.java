package com.pallas.logger;

import java.time.Instant;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

/**
 * Log4j2 appender that writes every log event into the shared {@link BacktraceBuffer}.
 *
 * <p>Register it on the root logger in {@code log4j2.xml} to capture records from all loggers,
 * regardless of the active log level:
 *
 * <pre>{@code
 * <Appenders>
 *   <BacktraceAppender name="Backtrace"/>
 * </Appenders>
 * <Loggers>
 *   <Root level="info">
 *     <AppenderRef ref="Backtrace"/>
 *   </Root>
 * </Loggers>
 * }</pre>
 */
@Plugin(name = "BacktraceAppender", category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE)
public final class BacktraceAppender extends AbstractAppender {

  private BacktraceAppender(String name, Filter filter) {
    super(name, filter, null, true, Property.EMPTY_ARRAY);
  }

  /**
   * Creates a new {@code BacktraceAppender} instance.
   *
   * @param name the appender name as declared in {@code log4j2.xml}
   * @return a new {@code BacktraceAppender}
   */
  @PluginFactory
  public static BacktraceAppender createAppender(String name) {
    return new BacktraceAppender(name, null);
  }

  @Override
  public void append(LogEvent event) {
    String message = event.getMessage() != null ? event.getMessage().getFormattedMessage() : "";
    Instant time = Instant.ofEpochMilli(event.getTimeMillis());
    BacktraceRecord record =
        new BacktraceRecord(
            event.getLoggerName(), event.getLevel(), message, time, event.getThrown());
    BacktraceBuffer.getInstance().add(record);
  }
}
