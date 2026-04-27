import 'package:logging/logging.dart';

import 'pallas_level.dart';
import 'pallas_log_buffer.dart';
import 'pallas_log_record.dart';

/// A logger with a fixed four-level severity API.
///
/// Each instance wraps a [Logger] from the `logging` package. Every log call
/// is also appended to the shared [PallasLogBuffer] so callers can inspect
/// recent history regardless of the active log level.
///
/// Configure the buffer capacity once at application start:
///
/// ```dart
/// PallasLogBuffer.configure(capacity: 200);
/// ```
///
/// Set the application-wide minimum log level once at application start:
///
/// ```dart
/// PallasLogger.setLevel(PallasLevel.info);
/// ```
///
/// Configure log output once at application start:
///
/// ```dart
/// Logger.root.onRecord.listen((record) {
///   // ignore: avoid_print
///   print('${record.level.name}: ${record.time}: ${record.message}');
/// });
/// ```
///
/// Then obtain a named logger per class or feature:
///
/// ```dart
/// final _log = PallasLogger('MyFeature');
/// _log.debug('Widget built');
/// _log.info('User signed in');
/// _log.warn('Token expiring soon');
/// _log.fatal('Unrecoverable error', error, stackTrace);
/// ```
class PallasLogger {
  /// Creates a [PallasLogger] with the given [name].
  ///
  /// The [name] is forwarded to the underlying [Logger] and appears in every
  /// log record, making log output easy to filter by feature.
  PallasLogger(String name) : _logger = Logger(name);

  /// Sets the minimum log level for all [PallasLogger] instances.
  ///
  /// Records below [level] are discarded by the underlying `logging` package
  /// and will not appear in listeners or be added to [PallasLogBuffer].
  ///
  /// Call once at application start, before any logging occurs:
  ///
  /// ```dart
  /// PallasLogger.setLevel(PallasLevel.info); // suppress debug in production
  /// ```
  static void setLevel(PallasLevel level) {
    Logger.root.level = level.loggingLevel;
  }

  final Logger _logger;

  void _log(
    Level level,
    Object? message,
    Object? error,
    StackTrace? stackTrace,
  ) {
    _logger.log(level, message, error, stackTrace);
    PallasLogBuffer.instance.add(
      PallasLogRecord(
        loggerName: _logger.name,
        level: level,
        message: message?.toString() ?? '',
        time: DateTime.now(),
        error: error,
        stackTrace: stackTrace,
      ),
    );
  }

  /// Logs a debug-level message (maps to [Level.FINE]).
  ///
  /// Use for detailed diagnostic information useful during development.
  void debug(Object? message, [Object? error, StackTrace? stackTrace]) =>
      _log(Level.FINE, message, error, stackTrace);

  /// Logs an informational message (maps to [Level.INFO]).
  ///
  /// Use for normal operational events such as user actions or lifecycle steps.
  void info(Object? message, [Object? error, StackTrace? stackTrace]) =>
      _log(Level.INFO, message, error, stackTrace);

  /// Logs a warning (maps to [Level.WARNING]).
  ///
  /// Use when something unexpected occurred but the application can continue.
  void warn(Object? message, [Object? error, StackTrace? stackTrace]) =>
      _log(Level.WARNING, message, error, stackTrace);

  /// Logs a fatal error (maps to [Level.SEVERE]).
  ///
  /// Use for critical failures from which the application cannot recover.
  void fatal(Object? message, [Object? error, StackTrace? stackTrace]) =>
      _log(Level.SEVERE, message, error, stackTrace);
}
