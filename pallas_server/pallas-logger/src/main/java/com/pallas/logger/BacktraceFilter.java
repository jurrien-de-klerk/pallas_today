package com.pallas.logger;

import java.time.Instant;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.filter.AbstractFilter;

/**
 * Log4j2 context-level filter that intercepts every log event <em>before</em> the root-logger level
 * check and writes it into the shared {@link BacktraceBuffer}.
 *
 * <p>Because it is registered as a context filter it sits between the application and Log4j2's own
 * level gating, so it captures records at all severity levels without altering what other appenders
 * receive. After recording the event it returns {@link Result#NEUTRAL} so normal Log4j2 processing
 * continues unchanged.
 *
 * <p>Register it at the top of {@code log4j2.xml} under {@code <Filters>}:
 *
 * <pre>{@code
 * <Configuration packages="com.pallas.logger">
 *   <Filters>
 *     <BacktraceFilter/>
 *   </Filters>
 *   <Appenders>
 *     <Console name="Console" .../>
 *   </Appenders>
 *   <Loggers>
 *     <Root level="info">
 *       <AppenderRef ref="Console"/>
 *     </Root>
 *   </Loggers>
 * </Configuration>
 * }</pre>
 */
@Plugin(
    name = "BacktraceFilter",
    category = Core.CATEGORY_NAME,
    elementType = Filter.ELEMENT_TYPE,
    printObject = true)
public final class BacktraceFilter extends AbstractFilter {

  /**
   * Thread-local flag set by {@link PallasBacktrace#backtrace(Logger)} during replay to prevent
   * replayed records from being re-buffered, which would mutate buffer history and violate the
   * non-destructive contract of {@link BacktraceBuffer#getRecords()}.
   */
  private static final ThreadLocal<Boolean> REPLAYING = ThreadLocal.withInitial(() -> false);

  /** Suppresses buffering on the calling thread. Call before starting a backtrace replay. */
  static void suppressForReplay() {
    REPLAYING.set(true);
  }

  /** Restores buffering on the calling thread. Call after a backtrace replay completes. */
  static void restoreAfterReplay() {
    REPLAYING.remove();
  }

  private BacktraceFilter() {
    super(Result.NEUTRAL, Result.NEUTRAL);
  }

  /** Creates a new {@code BacktraceFilter} instance. */
  @PluginFactory
  public static BacktraceFilter createFilter() {
    return new BacktraceFilter();
  }

  @Override
  public Result filter(LogEvent event) {
    if (Boolean.TRUE.equals(REPLAYING.get())) {
      return Result.NEUTRAL;
    }
    String message = event.getMessage() != null ? event.getMessage().getFormattedMessage() : "";
    Instant time = Instant.ofEpochMilli(event.getTimeMillis());
    BacktraceBuffer.getInstance()
        .add(
            new BacktraceRecord(
                event.getLoggerName(), event.getLevel(), message, time, event.getThrown()));
    return Result.NEUTRAL;
  }
}
