import 'package:flutter/material.dart';
import 'package:flutter_quill/flutter_quill.dart';
import 'package:pallas_logger/pallas_logger.dart';

import '../application/story_service.dart';

final _log = PallasLogger('MyBubbleStoryScreen');

class MyBubbleStoryScreen extends StatefulWidget {
  const MyBubbleStoryScreen({super.key});

  @override
  State<MyBubbleStoryScreen> createState() => _MyBubbleStoryScreenState();
}

class _MyBubbleStoryScreenState extends State<MyBubbleStoryScreen> {
  final _storyService = StoryService();
  List<Document>? _stories;
  bool _loading = true;

  @override
  void initState() {
    super.initState();
    _loadStories();
  }

  Future<void> _loadStories() async {
    _log.info('Loading stories');
    final stories = await _storyService.listStories();
    if (mounted) {
      setState(() {
        _stories = stories;
        _loading = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    if (_loading) {
      return const Center(child: CircularProgressIndicator());
    }

    final stories = _stories!;
    if (stories.isEmpty) {
      return const Center(child: Text('No stories yet.'));
    }

    return RefreshIndicator(
      onRefresh: _loadStories,
      child: ListView.separated(
        padding: const EdgeInsets.all(16),
        itemCount: stories.length,
        separatorBuilder: (_, _) => const Divider(),
        itemBuilder: (context, index) => _StoryCard(document: stories[index]),
      ),
    );
  }
}

/// A card that renders a single story as read-only rich text.
class _StoryCard extends StatefulWidget {
  const _StoryCard({required this.document});

  final Document document;

  @override
  State<_StoryCard> createState() => _StoryCardState();
}

class _StoryCardState extends State<_StoryCard> {
  late final QuillController _controller;
  late final FocusNode _focusNode;
  late final ScrollController _scrollController;

  @override
  void initState() {
    super.initState();
    _controller = QuillController(
      document: widget.document,
      selection: const TextSelection.collapsed(offset: 0),
      readOnly: true,
    );
    _focusNode = FocusNode(canRequestFocus: false);
    _scrollController = ScrollController();
  }

  @override
  void dispose() {
    _controller.dispose();
    _focusNode.dispose();
    _scrollController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(12),
        child: QuillEditor(
          controller: _controller,
          scrollController: _scrollController,
          focusNode: _focusNode,
          config: const QuillEditorConfig(showCursor: false, autoFocus: false),
        ),
      ),
    );
  }
}
