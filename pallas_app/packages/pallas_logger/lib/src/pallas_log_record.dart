import 'package:logging/logging.dart';

/// An immutable snapshot of a single log event captured by a [PallasLogger].
///
/// Stored inside [PallasLogBuffer]. The [level] uses the underlying
/// `logging` package's [Level] so consumers can compare against standard
/// constants such as [Level.INFO] or [Level.SEVERE].
class PallasLogRecord {
  /// Creates a log record.
  const PallasLogRecord({
    required this.loggerName,
    required this.level,
    required this.message,
    required this.time,
    this.error,
    this.stackTrace,
  });

  /// The name of the [PallasLogger] that produced this record.
  final String loggerName;

  /// Severity level of the record.
  final Level level;

  /// The log message.
  final String message;

  /// Wall-clock time at which the record was created.
  final DateTime time;

  /// Optional error object passed to the logging call.
  final Object? error;

  /// Optional stack trace passed to the logging call.
  final StackTrace? stackTrace;

  @override
  String toString() =>
      '${time.toIso8601String()} ${level.name} $loggerName: $message';
}
