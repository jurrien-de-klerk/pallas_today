import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_quill/flutter_quill.dart';
import 'package:flutter_quill_extensions/flutter_quill_extensions.dart';

import '../application/story_service.dart';

class PostStoryScreen extends StatefulWidget {
  const PostStoryScreen({super.key});

  @override
  State<PostStoryScreen> createState() => _PostStoryScreenState();
}

class _PostStoryScreenState extends State<PostStoryScreen> {
  late final QuillController _controller;
  final _storyService = StoryService();
  bool _publishing = false;

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

  Future<void> _publish() async {
    final document = _controller.document;
    final isEmpty = document.toPlainText().trim().isEmpty;
    setState(() => _publishing = true);
    final success = await _storyService.publishStory(document);
    if (!mounted) return;
    setState(() => _publishing = false);
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text(
          success
              ? 'Story published!'
              : isEmpty
              ? 'Write something before publishing.'
              : 'Failed to publish. Please try again.',
        ),
      ),
    );
    if (success) _controller.clear();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
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
        onPressed: _publishing ? null : _publish,
        label: _publishing
            ? const SizedBox(
                width: 16,
                height: 16,
                child: CircularProgressIndicator(strokeWidth: 2),
              )
            : const Text('Publish'),
        icon: _publishing ? const SizedBox.shrink() : const Icon(Icons.publish),
      ),
    );
  }
}
