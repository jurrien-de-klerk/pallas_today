package com.pallas.logger;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.time.Instant;
import org.apache.logging.log4j.Level;

/**
 * Immutable snapshot of a single log event captured by {@link BacktraceFilter}.
 *
 * @param loggerName name of the logger that produced the record
 * @param level severity level of the record
 * @param message formatted log message
 * @param time wall-clock time of the event
 * @param thrown optional throwable associated with the event, may be {@code null}
 */
public record BacktraceRecord(
    String loggerName, Level level, String message, Instant time, Throwable thrown) {

  @SuppressFBWarnings(
      value = "EI_EXPOSE_REP2",
      justification = "Throwable has no safe copy constructor; sharing the original is intentional")
  public BacktraceRecord {}

  /** Returns the throwable associated with this record, or {@code null} if none was provided. */
  @SuppressFBWarnings(
      value = "EI_EXPOSE_REP",
      justification = "Throwable has no safe copy constructor; callers need the original reference")
  @Override
  public Throwable thrown() {
    return thrown;
  }

  @Override
  public String toString() {
    return time + " " + level + " " + loggerName + ": " + message;
  }
}
