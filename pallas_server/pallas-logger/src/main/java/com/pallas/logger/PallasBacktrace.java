package com.pallas.logger;

import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

/**
 * Static utility that mirrors the {@code PallasLogger} contract for Java services.
 *
 * <p>Use Lombok's {@code @Log4j2} on any class to obtain a logger, then call these methods once at
 * application start to configure the logging behaviour:
 *
 * <pre>{@code
 * // Suppress DEBUG in production — only INFO and above pass through.
 * PallasBacktrace.setLevel(Level.INFO);
 *
 * // During backtrace replay, temporarily lower the root level to TRACE so
 * // all buffered records are visible regardless of the normal log level.
 * PallasBacktrace.setBacktraceLevel(Level.TRACE);
 *
 * // Optionally grow the ring buffer beyond the default 100 records.
 * BacktraceBuffer.configure(200);
 * }</pre>
 *
 * <p>When a crash is detected, call {@link #backtrace(Logger)} to replay all buffered records:
 *
 * <pre>{@code
 * @Log4j2
 * public class MyService {
 *   public void process() {
 *     try {
 *       ...
 *     } catch (Exception e) {
 *       log.error("Unhandled exception", e);
 *       PallasBacktrace.backtrace(log);
 *     }
 *   }
 * }
 * }</pre>
 */
public final class PallasBacktrace {

  private static volatile Level backtraceLevel = Level.FATAL;

  private PallasBacktrace() {}

  /**
   * Sets the application-wide minimum log level for all loggers.
   *
   * <p>Records below {@code level} are discarded by Log4j2 and will not be passed to any appender,
   * including {@link BacktraceAppender}. Call once at application start.
   *
   * @param level the minimum level to retain
   */
  public static void setLevel(Level level) {
    Configurator.setRootLevel(level);
  }

  /**
   * Sets the level used as a temporary root floor during {@link #backtrace(Logger)}.
   *
   * <p>Defaults to {@link Level#FATAL} when not explicitly configured. During a backtrace call the
   * root level is lowered to this value so that all buffered records — regardless of the normal log
   * level — are passed to listeners. The original root level is restored afterwards.
   *
   * @param level the floor level to apply during backtrace replay
   */
  public static void setBacktraceLevel(Level level) {
    backtraceLevel = level;
  }

  /**
   * Replays all records currently in {@link BacktraceBuffer} at their original levels without
   * modifying the buffer.
   *
   * <p>The root log level is temporarily lowered to the configured backtrace level (see {@link
   * #setBacktraceLevel(Level)}, default {@link Level#FATAL}) so that every buffered record is
   * visible to all appenders. The original root level is restored after replay completes.
   *
   * <p>Header and footer lines are logged at {@link Level#INFO} to delimit the backtrace output in
   * the log stream.
   *
   * @param logger the logger instance used to emit the replayed records
   */
  public static void backtrace(Logger logger) {
    List<BacktraceRecord> records = BacktraceBuffer.getInstance().getRecords();

    Level currentLevel = LogManager.getRootLogger().getLevel();
    Configurator.setRootLevel(backtraceLevel);

    logger.info("--- Backtrace start (replaying {} records) ---", records.size());
    for (BacktraceRecord record : records) {
      logger.log(
          record.level(),
          "[backtrace] {} {}: {}",
          record.time(),
          record.loggerName(),
          record.message(),
          record.thrown());
    }
    logger.info("--- Backtrace end ---");

    Configurator.setRootLevel(currentLevel);
  }
}
