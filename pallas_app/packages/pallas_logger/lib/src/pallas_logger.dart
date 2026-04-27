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
/// Set the backtrace level — the minimum root level applied temporarily during
/// a [PallasLogger.backtrace] call so all buffered records are visible
/// (defaults to [PallasLevel.fatal]):
///
/// ```dart
/// PallasLogger.setBacktraceLevel(PallasLevel.fatal);
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
/// // On crash, replay recent history at backtrace level:
/// _log.backtrace();
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

  /// Sets the severity level used as [Logger.root] floor during [backtrace].
  ///
  /// While [backtrace] runs, [Logger.root.level] is temporarily set to this
  /// value so that every buffered record — regardless of the normal log level
  /// — is passed to listeners. After [backtrace] completes the original root
  /// level is restored.
  ///
  /// Defaults to [PallasLevel.fatal] when not explicitly configured.
  ///
  /// ```dart
  /// PallasLogger.setBacktraceLevel(PallasLevel.warn);
  /// ```
  static void setBacktraceLevel(PallasLevel level) {
    _backtraceLevel = level;
  }

  static PallasLevel _backtraceLevel = PallasLevel.fatal;

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

  /// Replays all records currently in [PallasLogBuffer] at their original
  /// levels without modifying the buffer.
  ///
  /// To ensure all records are visible, [Logger.root.level] is temporarily
  /// lowered to the configured backtrace level (see [setBacktraceLevel],
  /// default [PallasLevel.fatal]) for the duration of the replay. The original
  /// root level is restored afterwards.
  ///
  /// A header and footer are logged at [Level.INFO] to delimit the backtrace
  /// output in the log stream.
  ///
  /// Call this when a crash is detected to surface the recent history through
  /// the normal log output pipeline:
  ///
  /// ```dart
  /// try {
  ///   ...
  /// } catch (e, st) {
  ///   _log.fatal('Unhandled exception', e, st);
  ///   _log.backtrace();
  /// }
  /// ```
  ///
  /// The buffer is not cleared, so [backtrace] can be called multiple times
  /// and will always replay the full current contents.
  void backtrace() {
    final records = PallasLogBuffer.instance.records;

    final currentLevel = Logger.root.level;
    Logger.root.level = _backtraceLevel.loggingLevel;

    _logger.info(
      '--- Backtrace start (replaying ${records.length} records) ---',
    );
    for (final record in records) {
      _logger.log(
        record.level,
        record.message,
        record.error,
        record.stackTrace,
      );
    }
    _logger.info('--- Backtrace end ---');

    Logger.root.level = currentLevel;
  }
}
