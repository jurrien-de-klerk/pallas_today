package com.pallas.logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.junit.jupiter.api.Test;

class BacktraceBufferTest {

  @Test
  void newBufferIsEmpty() {
    BacktraceBuffer buffer = new BacktraceBuffer(BacktraceBuffer.DEFAULT_CAPACITY);
    assertThat(buffer.isEmpty()).isTrue();
    assertThat(buffer.size()).isZero();
  }

  @Test
  void addIncreasesSize() {
    BacktraceBuffer buffer = new BacktraceBuffer(BacktraceBuffer.DEFAULT_CAPACITY);

    buffer.add(record("first"));
    buffer.add(record("second"));

    assertThat(buffer.size()).isEqualTo(2);
  }

  @Test
  void getRecordsReturnsOldestFirst() {
    BacktraceBuffer buffer = new BacktraceBuffer(10);

    buffer.add(record("alpha"));
    buffer.add(record("beta"));
    buffer.add(record("gamma"));

    List<BacktraceRecord> records = buffer.getRecords();
    assertThat(records)
        .extracting(BacktraceRecord::message)
        .containsExactly("alpha", "beta", "gamma");
  }

  @Test
  void getRecordsDoesNotModifyBuffer() {
    BacktraceBuffer buffer = new BacktraceBuffer(10);
    buffer.add(record("x"));

    buffer.getRecords();

    assertThat(buffer.size()).isEqualTo(1);
  }

  @Test
  void oldestRecordEvictedWhenFull() {
    BacktraceBuffer buffer = new BacktraceBuffer(3);

    buffer.add(record("one"));
    buffer.add(record("two"));
    buffer.add(record("three"));
    buffer.add(record("four")); // evicts "one"

    List<BacktraceRecord> records = buffer.getRecords();
    assertThat(records)
        .extracting(BacktraceRecord::message)
        .containsExactly("two", "three", "four");
  }

  @Test
  void capacityIsRespected() {
    assertThat(new BacktraceBuffer(5).getCapacity()).isEqualTo(5);
  }

  @Test
  void constructorWithZeroCapacityThrows() {
    assertThatThrownBy(() -> new BacktraceBuffer(0)).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void constructorWithNegativeCapacityThrows() {
    assertThatThrownBy(() -> new BacktraceBuffer(-1)).isInstanceOf(IllegalArgumentException.class);
  }

  // --- helpers ---

  private static BacktraceRecord record(String message) {
    return new BacktraceRecord("TestLogger", Level.INFO, message, Instant.now(), null);
  }
}
