package com.pallas.logger;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.Property;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PallasBacktraceTest {

  private static final Logger LOG = LogManager.getLogger(PallasBacktraceTest.class);

  /** In-memory appender that captures all LogEvents emitted during a test. */
  private CapturingAppender capturingAppender;

  private Level levelBeforeTest;

  @BeforeEach
  void setUp() {
    levelBeforeTest = LogManager.getRootLogger().getLevel();

    // Reset buffer and backtrace level state.
    BacktraceBuffer.configure(BacktraceBuffer.DEFAULT_CAPACITY);
    PallasBacktrace.setBacktraceLevel(Level.FATAL);
    PallasBacktrace.setLevel(Level.ALL);

    // Attach a capturing appender to the root logger so we can inspect emitted records.
    capturingAppender = CapturingAppender.create();
    org.apache.logging.log4j.core.Logger rootLogger =
        (org.apache.logging.log4j.core.Logger) LogManager.getRootLogger();
    rootLogger.addAppender(capturingAppender);
  }

  @AfterEach
  void tearDown() {
    org.apache.logging.log4j.core.Logger rootLogger =
        (org.apache.logging.log4j.core.Logger) LogManager.getRootLogger();
    rootLogger.removeAppender(capturingAppender);
    Configurator.setRootLevel(levelBeforeTest);
  }

  @Test
  void setLevelChangesRootLevel() {
    PallasBacktrace.setLevel(Level.ERROR);

    assertThat(LogManager.getRootLogger().getLevel()).isEqualTo(Level.ERROR);
  }

  @Test
  void backtraceReplaysBufferedRecords() {
    BacktraceBuffer.getInstance().add(record("first", Level.DEBUG));
    BacktraceBuffer.getInstance().add(record("second", Level.INFO));
    BacktraceBuffer.getInstance().add(record("third", Level.WARN));

    PallasBacktrace.setBacktraceLevel(Level.ALL);
    PallasBacktrace.backtrace(LOG);

    List<String> messages = capturingAppender.messages();
    assertThat(messages).anyMatch(m -> m.contains("first"));
    assertThat(messages).anyMatch(m -> m.contains("second"));
    assertThat(messages).anyMatch(m -> m.contains("third"));
  }

  @Test
  void backtraceEmitsHeaderAndFooter() {
    PallasBacktrace.setBacktraceLevel(Level.ALL);
    PallasBacktrace.backtrace(LOG);

    List<String> messages = capturingAppender.messages();
    assertThat(messages).anyMatch(m -> m.contains("Backtrace start"));
    assertThat(messages).anyMatch(m -> m.contains("Backtrace end"));
  }

  @Test
  void backtraceDoesNotModifyBuffer() {
    BacktraceBuffer.getInstance().add(record("keep me", Level.INFO));

    PallasBacktrace.setBacktraceLevel(Level.ALL);
    PallasBacktrace.backtrace(LOG);

    assertThat(BacktraceBuffer.getInstance().size()).isEqualTo(1);
  }

  @Test
  void rootLevelIsRestoredAfterBacktrace() {
    Configurator.setRootLevel(Level.WARN);
    PallasBacktrace.setBacktraceLevel(Level.ALL);

    PallasBacktrace.backtrace(LOG);

    assertThat(LogManager.getRootLogger().getLevel()).isEqualTo(Level.WARN);
  }

  @Test
  void backtraceOnEmptyBufferEmitsHeaderAndFooterOnly() {
    PallasBacktrace.setBacktraceLevel(Level.ALL);
    PallasBacktrace.backtrace(LOG);

    List<String> messages = capturingAppender.messages();
    assertThat(messages).hasSize(2); // header + footer
  }

  // --- helpers ---

  private static BacktraceRecord record(String message, Level level) {
    return new BacktraceRecord("TestLogger", level, message, Instant.now(), null);
  }

  /** Minimal Log4j2 appender that keeps a list of formatted message strings. */
  private static final class CapturingAppender extends AbstractAppender {

    private final List<String> captured = new ArrayList<>();

    private CapturingAppender() {
      super("CapturingAppender", null, null, true, Property.EMPTY_ARRAY);
    }

    static CapturingAppender create() {
      CapturingAppender appender = new CapturingAppender();
      appender.start();
      return appender;
    }

    @Override
    public void append(LogEvent event) {
      captured.add(event.getMessage().getFormattedMessage());
    }

    List<String> messages() {
      return List.copyOf(captured);
    }
  }
}
