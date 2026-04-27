import 'package:logging/logging.dart';
import 'package:pallas_logger/pallas_logger.dart';
import 'package:test/test.dart';

void main() {
  late PallasLogger log;
  final emitted = <LogRecord>[];

  setUpAll(() {
    // Wire up a listener on Logger.root so we can inspect emitted records.
    Logger.root.onRecord.listen(emitted.add);
  });

  setUp(() {
    emitted.clear();
    PallasLogBuffer.configure(capacity: 100);
    PallasLogger.setLevel(PallasLevel.debug);
    PallasLogger.setBacktraceLevel(PallasLevel.fatal);
    log = PallasLogger('TestFeature');
  });

  group('PallasLogger – log level methods', () {
    test('debug emits Level.FINE and adds to buffer', () {
      log.debug('d message');

      expect(emitted, hasLength(1));
      expect(emitted.first.level, equals(Level.FINE));
      expect(emitted.first.message, equals('d message'));
      expect(PallasLogBuffer.instance.length, equals(1));
    });

    test('info emits Level.INFO', () {
      log.info('i message');
      expect(emitted.first.level, equals(Level.INFO));
    });

    test('warn emits Level.WARNING', () {
      log.warn('w message');
      expect(emitted.first.level, equals(Level.WARNING));
    });

    test('fatal emits Level.SEVERE', () {
      log.fatal('f message');
      expect(emitted.first.level, equals(Level.SEVERE));
    });

    test('error and stackTrace are forwarded', () {
      final err = Exception('oops');
      final st = StackTrace.current;

      log.fatal('fail', err, st);

      expect(emitted.first.error, same(err));
      expect(emitted.first.stackTrace, same(st));
    });

    test('each call appends one record to the buffer', () {
      log.debug('a');
      log.info('b');
      log.warn('c');
      log.fatal('d');

      expect(PallasLogBuffer.instance.length, equals(4));
    });

    test('buffer record preserves logger name', () {
      log.info('named');
      expect(
        PallasLogBuffer.instance.records.first.loggerName,
        equals('TestFeature'),
      );
    });
  });

  group('PallasLogger.setLevel', () {
    test('records below level are not emitted', () {
      PallasLogger.setLevel(PallasLevel.warn);
      log.debug('suppressed');
      log.info('also suppressed');

      expect(emitted, isEmpty);
    });

    test('records at or above level are emitted', () {
      PallasLogger.setLevel(PallasLevel.warn);
      log.warn('visible');
      log.fatal('also visible');

      expect(emitted, hasLength(2));
    });
  });

  group('PallasLogger.backtrace', () {
    test('replays buffered records through the logger', () {
      PallasLogger.setBacktraceLevel(PallasLevel.debug);
      log.info('before crash');
      emitted.clear(); // only care about backtrace output

      log.backtrace();

      final messages = emitted.map((r) => r.message).toList();
      expect(messages, anyElement(contains('before crash')));
    });

    test('emits header and footer at INFO', () {
      PallasLogger.setBacktraceLevel(PallasLevel.debug);
      log.backtrace();

      final messages = emitted.map((r) => r.message).toList();
      expect(messages, anyElement(contains('Backtrace start')));
      expect(messages, anyElement(contains('Backtrace end')));
    });

    test('does not clear the buffer', () {
      log.info('stay');
      PallasLogger.setBacktraceLevel(PallasLevel.debug);
      log.backtrace();

      expect(PallasLogBuffer.instance.length, equals(1));
    });

    test('restores root level after replay', () {
      PallasLogger.setLevel(PallasLevel.warn);
      PallasLogger.setBacktraceLevel(PallasLevel.debug);

      log.backtrace();

      expect(Logger.root.level, equals(Level.WARNING));
    });
  });
}
