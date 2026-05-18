package com.pallas.logger;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.Property;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PallasLoggerTest {

  private static final PallasLogger LOG = PallasLogger.getLogger(PallasLoggerTest.class);

  private CapturingAppender capturingAppender;
  private Level levelBeforeTest;

  @BeforeEach
  void setUp() {
    levelBeforeTest = LogManager.getRootLogger().getLevel();
    PallasLogger.configure(BacktraceBuffer.DEFAULT_CAPACITY);
    Configurator.setRootLevel(Level.ALL);

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
    capturingAppender.stop();
    Configurator.setRootLevel(levelBeforeTest);
  }

  @Test
  void debugCapturesRecordInBuffer() {
    LOG.debug("debug message");

    assertThat(PallasLogger.BUFFER.getRecords())
        .extracting(BacktraceRecord::message)
        .contains("debug message");
  }

  @Test
  void infoCapturesRecordInBuffer() {
    LOG.info("info message");

    assertThat(PallasLogger.BUFFER.getRecords())
        .extracting(BacktraceRecord::message)
        .contains("info message");
  }

  @Test
  void warnCapturesRecordInBuffer() {
    LOG.warn("warn message");

    assertThat(PallasLogger.BUFFER.getRecords())
        .extracting(BacktraceRecord::message)
        .contains("warn message");
  }

  @Test
  void errorCapturesRecordInBuffer() {
    LOG.error("error message");

    assertThat(PallasLogger.BUFFER.getRecords())
        .extracting(BacktraceRecord::message)
        .contains("error message");
  }

  @Test
  void errorWithThrowableCapturesThrownInBuffer() {
    RuntimeException boom = new RuntimeException("boom");

    LOG.error("something went wrong", boom);

    BacktraceRecord record =
        PallasLogger.BUFFER.getRecords().stream()
            .filter(r -> r.message().equals("something went wrong"))
            .findFirst()
            .orElseThrow();
    assertThat(record.thrown()).isSameAs(boom);
    assertThat(record.level()).isEqualTo(Level.ERROR);
  }

  @Test
  void fatalCapturesRecordInBuffer() {
    LOG.fatal("fatal message");

    assertThat(PallasLogger.BUFFER.getRecords())
        .extracting(BacktraceRecord::message)
        .contains("fatal message");
  }

  @Test
  void fatalWithThrowableCapturesThrownInBuffer() {
    RuntimeException boom = new RuntimeException("fatal boom");

    LOG.fatal("system failure", boom);

    BacktraceRecord record =
        PallasLogger.BUFFER.getRecords().stream()
            .filter(r -> r.message().equals("system failure"))
            .findFirst()
            .orElseThrow();
    assertThat(record.thrown()).isSameAs(boom);
    assertThat(record.level()).isEqualTo(Level.FATAL);
  }

  @Test
  void parameterizedArgsAreFormattedInBuffer() {
    LOG.info("processing batch {} of {}", 3, 10);

    assertThat(PallasLogger.BUFFER.getRecords())
        .extracting(BacktraceRecord::message)
        .contains("processing batch 3 of 10");
  }

  @Test
  void trailingThrowableInArgsIsExtractedAsThrown() {
    RuntimeException ex = new RuntimeException("trailing");

    LOG.warn("oops {}", "value", ex);

    BacktraceRecord record =
        PallasLogger.BUFFER.getRecords().stream()
            .filter(r -> r.message().contains("oops value"))
            .findFirst()
            .orElseThrow();
    assertThat(record.thrown()).isSameAs(ex);
  }

  @Test
  void debugIsCapturedInBufferEvenWhenLog4jLevelIsInfo() {
    // Root level set to INFO: Log4j2 will drop the debug event, but the buffer must still capture
    // it because PallasLogger writes to the buffer before forwarding to the delegate.
    Configurator.setRootLevel(Level.INFO);

    LOG.debug("suppressed by log4j");

    assertThat(PallasLogger.BUFFER.getRecords())
        .extracting(BacktraceRecord::message)
        .contains("suppressed by log4j");
    assertThat(capturingAppender.messages()).doesNotContain("suppressed by log4j");
  }

  @Test
  void infoIsForwardedToLog4jAppender() {
    LOG.info("forwarded");

    assertThat(capturingAppender.messages()).contains("forwarded");
  }

  @Test
  void backtraceReplaysBufferViaLog4j() {
    PallasLogger.BUFFER.add(
        new BacktraceRecord(
            "TestLogger", Level.WARN, "buffered event", java.time.Instant.now(), null));

    PallasBacktrace.setBacktraceLevel(Level.ALL);
    LOG.backtrace();

    assertThat(capturingAppender.messages()).anyMatch(m -> m.contains("buffered event"));
  }

  @Test
  void getLoggerByNameSetsCorrectLoggerName() {
    PallasLogger named = PallasLogger.getLogger("com.pallas.MyService");
    named.info("named logger");

    assertThat(PallasLogger.BUFFER.getRecords())
        .extracting(BacktraceRecord::loggerName)
        .contains("com.pallas.MyService");
  }

  // --- helpers ---

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
