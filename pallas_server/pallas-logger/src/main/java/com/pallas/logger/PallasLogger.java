package com.pallas.logger;

import java.time.Instant;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ParameterizedMessage;

/**
 * Drop-in logger that combines a standard Log4j2 logger with an automatic backtrace buffer.
 *
 * <p>Every log call is captured in the shared {@link #BUFFER} <em>and</em> forwarded to the
 * underlying Log4j2 logger. Because the capture happens unconditionally in this wrapper — before
 * Log4j2's own level gate — the buffer always contains the full record history regardless of the
 * active log level, identical to the previous {@code BacktraceFilter} behaviour.
 *
 * <p>Usage:
 *
 * <pre>{@code
 * private static final PallasLogger log = PallasLogger.getLogger(MyService.class);
 *
 * // Normal logging — captured in buffer and forwarded to appenders.
 * log.debug("processing item {}", id);
 * log.info("request complete");
 *
 * // On failure, replay the entire buffer via Log4j2.
 * log.backtrace();
 * }</pre>
 *
 * <p>The shared buffer capacity is configurable via {@link #configure(int)}.
 */
public final class PallasLogger {

  /**
   * Shared backtrace buffer written by every {@code PallasLogger} instance.
   *
   * <p>Package-private so tests can inspect and reset it directly. Production code outside this
   * class must not write to the buffer.
   */
  @SuppressWarnings("PMD.AssignmentToNonFinalStatic")
  static BacktraceBuffer BUFFER = new BacktraceBuffer(BacktraceBuffer.DEFAULT_CAPACITY);

  private final Logger delegate;

  private PallasLogger(Logger delegate) {
    this.delegate = delegate;
  }

  /**
   * Replaces the shared buffer with a fresh one of the given {@code capacity}.
   *
   * <p>Any existing records are discarded. Call once at application start before any logging
   * occurs.
   *
   * @param capacity maximum number of records to retain; must be positive
   * @throws IllegalArgumentException when {@code capacity} is not a positive integer
   */
  public static synchronized void configure(int capacity) {
    BUFFER = new BacktraceBuffer(capacity);
  }

  /** Returns a {@code PallasLogger} for the given class. */
  public static PallasLogger getLogger(Class<?> clazz) {
    return new PallasLogger(LogManager.getLogger(clazz));
  }

  /** Returns a {@code PallasLogger} for the given name. */
  public static PallasLogger getLogger(String name) {
    return new PallasLogger(LogManager.getLogger(name));
  }

  // -------------------------------------------------------------------------
  // Logging methods
  // -------------------------------------------------------------------------

  /** Logs at DEBUG level. */
  public void debug(String message) {
    capture(Level.DEBUG, message, null);
    delegate.debug(message);
  }

  /** Logs at DEBUG level with parameterised arguments ({@code {}} placeholders). */
  public void debug(String message, Object... args) {
    captureParameterized(Level.DEBUG, message, args);
    delegate.debug(message, args);
  }

  /** Logs at INFO level. */
  public void info(String message) {
    capture(Level.INFO, message, null);
    delegate.info(message);
  }

  /** Logs at INFO level with parameterised arguments. */
  public void info(String message, Object... args) {
    captureParameterized(Level.INFO, message, args);
    delegate.info(message, args);
  }

  /** Logs at WARN level. */
  public void warn(String message) {
    capture(Level.WARN, message, null);
    delegate.warn(message);
  }

  /** Logs at WARN level with parameterised arguments. */
  public void warn(String message, Object... args) {
    captureParameterized(Level.WARN, message, args);
    delegate.warn(message, args);
  }

  /** Logs at ERROR level. */
  public void error(String message) {
    capture(Level.ERROR, message, null);
    delegate.error(message);
  }

  /** Logs at ERROR level with parameterised arguments. */
  public void error(String message, Object... args) {
    captureParameterized(Level.ERROR, message, args);
    delegate.error(message, args);
  }

  /** Logs at ERROR level with an explicit {@link Throwable}. */
  public void error(String message, Throwable thrown) {
    capture(Level.ERROR, message, thrown);
    delegate.error(message, thrown);
  }

  /** Logs at FATAL level. */
  public void fatal(String message) {
    capture(Level.FATAL, message, null);
    delegate.fatal(message);
  }

  /** Logs at FATAL level with parameterised arguments. */
  public void fatal(String message, Object... args) {
    captureParameterized(Level.FATAL, message, args);
    delegate.fatal(message, args);
  }

  /** Logs at FATAL level with an explicit {@link Throwable}. */
  public void fatal(String message, Throwable thrown) {
    capture(Level.FATAL, message, thrown);
    delegate.fatal(message, thrown);
  }

  // -------------------------------------------------------------------------
  // Backtrace
  // -------------------------------------------------------------------------

  /**
   * Replays all records currently held in {@link BacktraceBuffer} via the underlying Log4j2 logger.
   *
   * <p>Delegates to {@link PallasBacktrace#backtrace(Logger, BacktraceBuffer)}.
   */
  public void backtrace() {
    PallasBacktrace.backtrace(delegate, BUFFER);
  }

  // -------------------------------------------------------------------------
  // Internal helpers
  // -------------------------------------------------------------------------

  private void capture(Level level, String message, Throwable thrown) {
    BUFFER.add(new BacktraceRecord(delegate.getName(), level, message, Instant.now(), thrown));
  }

  /**
   * Formats {@code message} with {@code args} using Log4j2's {@link ParameterizedMessage} — which
   * also extracts a trailing {@link Throwable} argument — before storing the record.
   */
  private void captureParameterized(Level level, String message, Object[] args) {
    ParameterizedMessage pm = new ParameterizedMessage(message, args);
    BUFFER.add(
        new BacktraceRecord(
            delegate.getName(), level, pm.getFormattedMessage(), Instant.now(), pm.getThrowable()));
  }
}
