import 'dart:ui';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:flutter_quill/flutter_quill.dart';
import 'package:logging/logging.dart';
import 'package:pallas_logger/pallas_logger.dart';

import 'presentation/home_shell.dart';

final _log = PallasLogger('main');

void main() {
  PallasLogger.setLevel(PallasLevel.debug);
  Logger.root.onRecord.listen(
    // ignore: avoid_print
    (record) => print(
      '${record.level.name}: ${record.time}: [${record.loggerName}] ${record.message}',
    ),
  );

  FlutterError.onError = (FlutterErrorDetails details) {
    _log.fatal('Uncaught Flutter error', details.exception, details.stack);
    _log.backtrace();
  };

  PlatformDispatcher.instance.onError = (Object error, StackTrace stack) {
    _log.fatal('Uncaught async error', error, stack);
    _log.backtrace();
    return true;
  };

  _log.info('Application starting');
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Pallas Today',
      localizationsDelegates: const [
        GlobalMaterialLocalizations.delegate,
        GlobalWidgetsLocalizations.delegate,
        GlobalCupertinoLocalizations.delegate,
        FlutterQuillLocalizations.delegate,
      ],
      supportedLocales: const [Locale('en')],
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.indigo),
      ),
      home: const HomeShell(),
    );
  }
}
