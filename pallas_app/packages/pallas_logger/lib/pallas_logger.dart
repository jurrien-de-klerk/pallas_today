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
library pallas_logger;

export 'src/pallas_logger.dart';
