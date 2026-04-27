package com.pallas.logger;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import org.apache.logging.log4j.Level;
import org.junit.jupiter.api.Test;

class BacktraceRecordTest {

  @Test
  void accessorsReturnConstructorValues() {
    Instant now = Instant.now();
    RuntimeException ex = new RuntimeException("boom");

    BacktraceRecord record = new BacktraceRecord("MyLogger", Level.WARN, "a message", now, ex);

    assertThat(record.loggerName()).isEqualTo("MyLogger");
    assertThat(record.level()).isEqualTo(Level.WARN);
    assertThat(record.message()).isEqualTo("a message");
    assertThat(record.time()).isEqualTo(now);
    assertThat(record.thrown()).isSameAs(ex);
  }

  @Test
  void thrownCanBeNull() {
    BacktraceRecord record =
        new BacktraceRecord("L", Level.INFO, "msg", Instant.EPOCH, null);

    assertThat(record.thrown()).isNull();
  }

  @Test
  void toStringContainsLevelLoggerNameAndMessage() {
    Instant instant = Instant.parse("2024-01-01T00:00:00Z");
    BacktraceRecord record = new BacktraceRecord("SvcLogger", Level.ERROR, "bad thing", instant, null);

    assertThat(record.toString())
        .contains("ERROR")
        .contains("SvcLogger")
        .contains("bad thing")
        .contains("2024-01-01");
  }

  @Test
  void equalRecordsAreEqual() {
    Instant now = Instant.EPOCH;
    BacktraceRecord a = new BacktraceRecord("L", Level.INFO, "msg", now, null);
    BacktraceRecord b = new BacktraceRecord("L", Level.INFO, "msg", now, null);

    assertThat(a).isEqualTo(b);
    assertThat(a.hashCode()).isEqualTo(b.hashCode());
  }
}
