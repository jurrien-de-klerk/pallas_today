import 'package:logging/logging.dart';

/// The four severity levels understood by [PallasLogger].
///
/// Use [PallasLogger.setLevel] to apply one of these application-wide.
enum PallasLevel {
  /// Detailed diagnostic information — maps to [Level.FINE].
  debug,

  /// Normal operational events — maps to [Level.INFO].
  info,

  /// Unexpected but recoverable events — maps to [Level.WARNING].
  warn,

  /// Non-fatal malfunction that must be investigated — maps to [Level.SEVERE].
  error,

  /// Critical, unrecoverable failure that crashes the application — maps to [Level.SHOUT].
  fatal;

  /// The corresponding [Level] from the `logging` package.
  Level get loggingLevel => switch (this) {
    PallasLevel.debug => Level.FINE,
    PallasLevel.info => Level.INFO,
    PallasLevel.warn => Level.WARNING,
    PallasLevel.error => Level.SEVERE,
    PallasLevel.fatal => Level.SHOUT,
  };
}
