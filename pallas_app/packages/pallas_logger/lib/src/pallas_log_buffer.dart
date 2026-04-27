import 'dart:collection';

import 'pallas_log_record.dart';

/// Default number of records the buffer holds when not explicitly configured.
const int _kDefaultCapacity = 100;

/// A shared, process-wide FIFO ring buffer that captures every record logged
/// by any [PallasLogger] instance, regardless of level.
///
/// ## Configuration
///
/// Call [PallasLogBuffer.configure] once at application start — before any
/// logger is used — to set the buffer capacity:
///
/// ```dart
/// PallasLogBuffer.configure(capacity: 200);
/// ```
///
/// The default capacity is $_kDefaultCapacity records.
///
/// ## Reading the buffer
///
/// ```dart
/// // Snapshot of all buffered records (oldest first).
/// final records = PallasLogBuffer.instance.records;
///
/// // Drain and clear in one step.
/// final drained = PallasLogBuffer.instance.drain();
/// ```
class PallasLogBuffer {
  PallasLogBuffer._({required int capacity}) : _capacity = capacity;

  static PallasLogBuffer? _instance;

  /// The singleton buffer instance.
  ///
  /// Created with the default capacity on first access. Call [configure]
  /// before accessing this getter if a custom capacity is required.
  static PallasLogBuffer get instance =>
      _instance ??= PallasLogBuffer._(capacity: _kDefaultCapacity);

  /// Configures (or re-creates) the singleton buffer with [capacity] slots.
  ///
  /// Calling this after records have been added discards all existing records.
  /// Must be called before any logger emits records to take effect.
  ///
  /// Throws [ArgumentError] when [capacity] is not a positive integer.
  static void configure({required int capacity}) {
    if (capacity <= 0) {
      throw ArgumentError.value(
        capacity,
        'capacity',
        'Must be a positive integer.',
      );
    }
    _instance = PallasLogBuffer._(capacity: capacity);
  }

  final int _capacity;
  final Queue<PallasLogRecord> _queue = Queue<PallasLogRecord>();

  /// Maximum number of records the buffer holds.
  int get capacity => _capacity;

  /// Number of records currently in the buffer.
  int get length => _queue.length;

  /// Whether the buffer holds no records.
  bool get isEmpty => _queue.isEmpty;

  /// An unmodifiable snapshot of buffered records, oldest first.
  List<PallasLogRecord> get records =>
      List<PallasLogRecord>.unmodifiable(_queue);

  /// Removes and returns all buffered records (oldest first), leaving the
  /// buffer empty.
  List<PallasLogRecord> drain() {
    final snapshot = List<PallasLogRecord>.of(_queue);
    _queue.clear();
    return snapshot;
  }

  /// Appends [record] to the buffer.
  ///
  /// When the buffer is full the oldest record is evicted to make room.
  void add(PallasLogRecord record) {
    if (_queue.length >= _capacity) {
      _queue.removeFirst();
    }
    _queue.addLast(record);
  }
}
