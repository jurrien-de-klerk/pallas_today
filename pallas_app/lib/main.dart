import 'package:flutter/material.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:flutter_quill/flutter_quill.dart';
import 'package:flutter_quill_extensions/flutter_quill_extensions.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Pallas Editor',
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
      home: const EditorScreen(),
    );
  }
}

class EditorScreen extends StatefulWidget {
  const EditorScreen({super.key});

  @override
  State<EditorScreen> createState() => _EditorScreenState();
}

class _EditorScreenState extends State<EditorScreen> {
  late final QuillController _controller;

  @override
  void initState() {
    super.initState();
    _controller = QuillController.basic();
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        title: const Text('Write Story'),
      ),
      body: SafeArea(
        child: Column(
          children: [
            QuillSimpleToolbar(
              controller: _controller,
              config: QuillSimpleToolbarConfig(
                showStrikeThrough: false,
                showInlineCode: false,
                showSubscript: false,
                showSuperscript: false,
                showBackgroundColorButton: false,
                showListCheck: false,
                showCodeBlock: false,
                embedButtons: [
                  ...FlutterQuillEmbeds.toolbarButtons(
                    videoButtonOptions: null,
                    cameraButtonOptions: null,
                  ),
                ],
              ),
            ),
            const Divider(height: 1),
            Expanded(
              child: Padding(
                padding: const EdgeInsets.all(16),
                child: QuillEditor.basic(
                  controller: _controller,
                  config: QuillEditorConfig(
                    placeholder: 'Start writing your story...',
                    embedBuilders: kIsWeb
                        ? FlutterQuillEmbeds.editorWebBuilders()
                        : FlutterQuillEmbeds.editorBuilders(),
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () {},
        label: const Text('Publish'),
        icon: const Icon(Icons.publish),
      ),
    );
  }
}
