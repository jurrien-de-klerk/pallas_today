import 'package:logging/logging.dart';
import 'package:pallas_logger/pallas_logger.dart';
import 'package:test/test.dart';

PallasLogRecord _record(String message, {Level? level}) => PallasLogRecord(
  loggerName: 'TestLogger',
  level: level ?? Level.INFO,
  message: message,
  time: DateTime(2024),
);

void main() {
  setUp(() {
    // Reset singleton before each test.
    PallasLogBuffer.configure(capacity: PallasLogBuffer.instance.capacity);
  });

  group('PallasLogBuffer.configure', () {
    test('throws when capacity is zero', () {
      expect(() => PallasLogBuffer.configure(capacity: 0), throwsArgumentError);
    });

    test('throws when capacity is negative', () {
      expect(
        () => PallasLogBuffer.configure(capacity: -5),
        throwsArgumentError,
      );
    });

    test('discards existing records', () {
      PallasLogBuffer.instance.add(_record('old'));
      PallasLogBuffer.configure(capacity: 10);
      expect(PallasLogBuffer.instance.isEmpty, isTrue);
    });

    test('sets custom capacity', () {
      PallasLogBuffer.configure(capacity: 42);
      expect(PallasLogBuffer.instance.capacity, equals(42));
    });
  });

  group('PallasLogBuffer add / records', () {
    setUp(() => PallasLogBuffer.configure(capacity: 10));

    test('starts empty', () {
      expect(PallasLogBuffer.instance.isEmpty, isTrue);
      expect(PallasLogBuffer.instance.length, isZero);
    });

    test('add increases length', () {
      PallasLogBuffer.instance.add(_record('a'));
      PallasLogBuffer.instance.add(_record('b'));
      expect(PallasLogBuffer.instance.length, equals(2));
    });

    test('records returns oldest first', () {
      PallasLogBuffer.instance.add(_record('alpha'));
      PallasLogBuffer.instance.add(_record('beta'));
      PallasLogBuffer.instance.add(_record('gamma'));

      final messages = PallasLogBuffer.instance.records
          .map((r) => r.message)
          .toList();
      expect(messages, equals(['alpha', 'beta', 'gamma']));
    });

    test('records is non-destructive', () {
      PallasLogBuffer.instance.add(_record('x'));
      PallasLogBuffer.instance.records; // read once
      expect(PallasLogBuffer.instance.length, equals(1));
    });

    test('evicts oldest when full', () {
      PallasLogBuffer.configure(capacity: 3);
      final buffer = PallasLogBuffer.instance;

      buffer.add(_record('one'));
      buffer.add(_record('two'));
      buffer.add(_record('three'));
      buffer.add(_record('four')); // evicts 'one'

      expect(
        buffer.records.map((r) => r.message).toList(),
        equals(['two', 'three', 'four']),
      );
    });
  });

  group('PallasLogBuffer.drain', () {
    setUp(() => PallasLogBuffer.configure(capacity: 10));

    test('returns all records oldest first', () {
      PallasLogBuffer.instance.add(_record('p'));
      PallasLogBuffer.instance.add(_record('q'));

      final drained = PallasLogBuffer.instance
          .drain()
          .map((r) => r.message)
          .toList();
      expect(drained, equals(['p', 'q']));
    });

    test('leaves buffer empty', () {
      PallasLogBuffer.instance.add(_record('z'));
      PallasLogBuffer.instance.drain();
      expect(PallasLogBuffer.instance.isEmpty, isTrue);
    });
  });
}
