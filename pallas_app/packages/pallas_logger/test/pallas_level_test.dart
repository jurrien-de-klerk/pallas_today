import 'package:logging/logging.dart';
import 'package:pallas_logger/pallas_logger.dart';
import 'package:test/test.dart';

void main() {
  group('PallasLevel', () {
    test('debug maps to Level.FINE', () {
      expect(PallasLevel.debug.loggingLevel, equals(Level.FINE));
    });

    test('info maps to Level.INFO', () {
      expect(PallasLevel.info.loggingLevel, equals(Level.INFO));
    });

    test('warn maps to Level.WARNING', () {
      expect(PallasLevel.warn.loggingLevel, equals(Level.WARNING));
    });

    test('error maps to Level.SEVERE', () {
      expect(PallasLevel.error.loggingLevel, equals(Level.SEVERE));
    });

    test('fatal maps to Level.SHOUT', () {
      expect(PallasLevel.fatal.loggingLevel, equals(Level.SHOUT));
    });

    test('all five levels are distinct', () {
      final levels = PallasLevel.values.map((l) => l.loggingLevel).toSet();
      expect(levels, hasLength(5));
    });
  });
}
