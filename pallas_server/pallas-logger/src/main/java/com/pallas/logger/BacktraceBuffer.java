package com.pallas.logger;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

/**
 * Thread-safe shared FIFO ring buffer that captures every {@link BacktraceRecord} written by any
 * {@link PallasLogger} instance.
 *
 * <p>All {@link PallasLogger} instances share one static buffer. Configure the capacity once at
 * application start -- before any logging occurs:
 *
 * <pre>{@code
 * BacktraceBuffer.configure(200);
 * }</pre>
 *
 * The default capacity is {@value #DEFAULT_CAPACITY} records. When the buffer is full the oldest
 * record is evicted to make room for the new one.
 */
public final class BacktraceBuffer {

  /** Default number of records held when capacity is not explicitly configured. */
  public static final int DEFAULT_CAPACITY = 100;

  // Shared static buffer - all PallasLogger instances write here.
  @SuppressWarnings("PMD.AssignmentToNonFinalStatic")
  private static BacktraceBuffer instance = new BacktraceBuffer(DEFAULT_CAPACITY);

  private final int capacity;
  private final Deque<BacktraceRecord> queue;

  private BacktraceBuffer(int capacity) {
    this.capacity = capacity;
    this.queue = new ArrayDeque<>(capacity);
  }

  /**
   * Returns the shared buffer instance.
   *
   * <p>The returned reference is intentionally the live shared instance; callers need direct access
   * to add and read records.
   */
  @SuppressFBWarnings(
      value = "MS_EXPOSE_REP",
      justification =
          "Intentional shared mutable instance; callers need direct access to add records")
  public static BacktraceBuffer getInstance() {
    return instance;
  }

  /**
   * Configures (or re-creates) the buffer with the given {@code capacity}.
   *
   * <p>Any existing records are discarded. Must be called before any logger emits records to take
   * effect.
   *
   * @param capacity maximum number of records to retain; must be positive
   * @throws IllegalArgumentException when {@code capacity} is not a positive integer
   */
  public static void configure(int capacity) {
    if (capacity <= 0) {
      throw new IllegalArgumentException("capacity must be a positive integer, got: " + capacity);
    }
    synchronized (BacktraceBuffer.class) {
      instance = new BacktraceBuffer(capacity);
    }
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
