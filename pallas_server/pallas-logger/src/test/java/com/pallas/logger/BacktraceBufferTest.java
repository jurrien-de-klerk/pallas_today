package com.pallas.logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BacktraceBufferTest {

  @BeforeEach
  void resetSingleton() {
    // Reset to default capacity before each test so tests are independent.
    BacktraceBuffer.configure(BacktraceBuffer.DEFAULT_CAPACITY);
  }

  @Test
  void newBufferIsEmpty() {
    assertThat(BacktraceBuffer.getInstance().isEmpty()).isTrue();
    assertThat(BacktraceBuffer.getInstance().size()).isZero();
  }

  @Test
  void addIncreasesSize() {
    BacktraceBuffer buffer = BacktraceBuffer.getInstance();

    buffer.add(record("first"));
    buffer.add(record("second"));

    assertThat(buffer.size()).isEqualTo(2);
  }

  @Test
  void getRecordsReturnsOldestFirst() {
    BacktraceBuffer.configure(10);
    BacktraceBuffer buffer = BacktraceBuffer.getInstance();

    buffer.add(record("alpha"));
    buffer.add(record("beta"));
    buffer.add(record("gamma"));

    List<BacktraceRecord> records = buffer.getRecords();
    assertThat(records).extracting(BacktraceRecord::message)
        .containsExactly("alpha", "beta", "gamma");
  }

  @Test
  void getRecordsDoesNotModifyBuffer() {
    BacktraceBuffer.configure(10);
    BacktraceBuffer buffer = BacktraceBuffer.getInstance();
    buffer.add(record("x"));

    buffer.getRecords();

    assertThat(buffer.size()).isEqualTo(1);
  }

  @Test
  void oldestRecordEvictedWhenFull() {
    BacktraceBuffer.configure(3);
    BacktraceBuffer buffer = BacktraceBuffer.getInstance();

    buffer.add(record("one"));
    buffer.add(record("two"));
    buffer.add(record("three"));
    buffer.add(record("four")); // evicts "one"

    List<BacktraceRecord> records = buffer.getRecords();
    assertThat(records).extracting(BacktraceRecord::message)
        .containsExactly("two", "three", "four");
  }

  @Test
  void capacityIsRespected() {
    BacktraceBuffer.configure(5);
    assertThat(BacktraceBuffer.getInstance().getCapacity()).isEqualTo(5);
  }

  @Test
  void configureWithZeroCapacityThrows() {
    assertThatThrownBy(() -> BacktraceBuffer.configure(0))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void configureWithNegativeCapacityThrows() {
    assertThatThrownBy(() -> BacktraceBuffer.configure(-1))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void configureDiscardsExistingRecords() {
    BacktraceBuffer.getInstance().add(record("old"));

    BacktraceBuffer.configure(10);

    assertThat(BacktraceBuffer.getInstance().isEmpty()).isTrue();
  }

  // --- helpers ---

  private static BacktraceRecord record(String message) {
    return new BacktraceRecord("TestLogger", Level.INFO, message, Instant.now(), null);
  }
}
