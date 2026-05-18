package com.pallas.logger;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

/**
 * Thread-safe FIFO ring buffer that captures {@link BacktraceRecord} entries.
 *
 * <p>Instances are created directly via the constructor. The single shared instance used by all
 * {@link PallasLogger} loggers lives as a static field on {@link PallasLogger}; configure its
 * capacity via {@link PallasLogger#configure(int)}.
 *
 * <p>The default capacity is {@value #DEFAULT_CAPACITY} records. When the buffer is full the oldest
 * record is evicted to make room for the new one.
 */
public final class BacktraceBuffer {

  /** Default number of records held when capacity is not explicitly configured. */
  public static final int DEFAULT_CAPACITY = 100;

  private final int capacity;
  private final Deque<BacktraceRecord> queue;

  /**
   * Creates a new buffer with the given {@code capacity}.
   *
   * @param capacity maximum number of records to retain; must be positive
   * @throws IllegalArgumentException when {@code capacity} is not a positive integer
   */
  public BacktraceBuffer(int capacity) {
    if (capacity <= 0) {
      throw new IllegalArgumentException("capacity must be a positive integer, got: " + capacity);
    }
    this.capacity = capacity;
    this.queue = new ArrayDeque<>(capacity);
  }

  /** Returns the maximum number of records this buffer holds. */
  public int getCapacity() {
    return capacity;
  }

  /** Returns the number of records currently in the buffer. */
  public synchronized int size() {
    return queue.size();
  }

  /** Returns {@code true} when the buffer holds no records. */
  public synchronized boolean isEmpty() {
    return queue.isEmpty();
  }

  /**
   * Returns an unmodifiable snapshot of all buffered records, oldest first.
   *
   * <p>The buffer is not modified by this call.
   */
  public synchronized List<BacktraceRecord> getRecords() {
    return List.copyOf(new ArrayList<>(queue));
  }

  /**
   * Appends {@code record} to the buffer.
   *
   * <p>When the buffer is full the oldest record is evicted to make room.
   */
  public synchronized void add(BacktraceRecord record) {
    Objects.requireNonNull(record, "record must not be null");
    if (queue.size() >= capacity) {
      queue.removeFirst();
    }
    queue.addLast(record);
  }
}
