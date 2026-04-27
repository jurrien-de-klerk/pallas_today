/// Opinionated logger package for pallas_app.
///
/// Wraps the [logging](https://pub.dev/packages/logging) package and exposes
/// a fixed four-level API:
///
/// | pallas_logger | logging    |
/// |---------------|------------|
/// | [PallasLogger.debug] | [Level.FINE]    |
/// | [PallasLogger.info]  | [Level.INFO]    |
/// | [PallasLogger.warn]  | [Level.WARNING] |
/// | [PallasLogger.fatal] | [Level.SEVERE]  |
///
/// Every log call also appends a [PallasLogRecord] to the shared
/// [PallasLogBuffer], enabling backtrace inspection of recent history.
library pallas_logger;

export 'src/pallas_level.dart';
export 'src/pallas_log_buffer.dart';
export 'src/pallas_log_record.dart';
export 'src/pallas_logger.dart';
