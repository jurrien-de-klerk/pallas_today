import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_quill/flutter_quill.dart';
import 'package:pallas_logger/pallas_logger.dart';

import '../application/story_service.dart';
import '../domain/story.dart';

final _log = PallasLogger('MyBubbleStoryScreen');

class StoriesNearMeScreen extends StatefulWidget {
  const StoriesNearMeScreen({super.key});

  @override
  State<StoriesNearMeScreen> createState() => _StoriesNearMeScreenState();
}

class _StoriesNearMeScreenState extends State<StoriesNearMeScreen> {
  static const _pageSize = 20;

  late final StoryService _storyService;

  @override
  void initState() {
    super.initState();
    _storyService = StoryService();
    _loadStories();
  }

  @override
  void dispose() {
    _storyService.close();
    super.dispose();
  }

  Future<void> _loadStories() async {
    _log.info('Loading stories');
    await _storyService.loadStories(offset: 0, count: _pageSize);
  }

  @override
  Widget build(BuildContext context) {
    return BlocBuilder<StoryService, StoryServiceState>(
      bloc: _storyService,
      builder: (context, state) {
        if (state.isInitialLoading) {
          return const Center(child: CircularProgressIndicator());
        }

        if (state.error != null && state.stories.isEmpty) {
          return Center(child: Text('Failed to load stories: ${state.error}'));
        }

        if (state.stories.isEmpty) {
          return RefreshIndicator(
            onRefresh: _loadStories,
            child: ListView(
              physics: const AlwaysScrollableScrollPhysics(),
              children: const [
                SizedBox(height: 160),
                Center(child: Text('No stories yet.')),
              ],
            ),
          );
        }

        return RefreshIndicator(
          onRefresh: _loadStories,
          child: ListView.separated(
            padding: const EdgeInsets.all(16),
            itemCount: state.stories.length,
            separatorBuilder: (_, _) => const Divider(),
            itemBuilder: (context, index) {
              final story = state.stories[index];
              final member = state.membersById[story.authorId];
              return _StoryCard(
                document: _storyDocument(story),
                authorName: member?.name,
                isMemberLoading: state.isLoadingMembers && member == null,
              );
            },
          ),
        );
      },
    );
  }

  Document _storyDocument(Story story) {
    final deltaJson = story.content
        .map((op) => Map<String, dynamic>.from(op))
        .toList(growable: true);

    if (deltaJson.isEmpty) {
      deltaJson.add({'insert': '\n'});
    } else {
      final lastInsert = deltaJson.last['insert'];
      if (lastInsert is! String || !lastInsert.endsWith('\n')) {
        deltaJson.add({'insert': '\n'});
      }
    }

    return Document.fromJson(deltaJson);
  }
}

/// A card that renders a single story as read-only rich text.
class _StoryCard extends StatefulWidget {
  const _StoryCard({
    required this.document,
    required this.authorName,
    required this.isMemberLoading,
  });

  final Document document;
  final String? authorName;
  final bool isMemberLoading;

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
      clipBehavior: Clip.antiAlias,
      child: Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          Container(
            padding: const EdgeInsets.all(16),
            color: Theme.of(context).colorScheme.surfaceContainerHighest,
            child: Row(
              children: [
                CircleAvatar(
                  radius: 18,
                  backgroundColor: Theme.of(
                    context,
                  ).colorScheme.primaryContainer,
                  foregroundColor: Theme.of(
                    context,
                  ).colorScheme.onPrimaryContainer,
                  child: const Icon(Icons.article_outlined, size: 18),
                ),
                const SizedBox(width: 12),
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        widget.authorName ?? 'Story',
                        style: const TextStyle(fontWeight: FontWeight.w600),
                      ),
                      if (widget.isMemberLoading)
                        Text(
                          'Loading author...',
                          style: Theme.of(context).textTheme.bodySmall,
                        ),
                    ],
                  ),
                ),
              ],
            ),
          ),
          const Divider(height: 1),
          Padding(
            padding: const EdgeInsets.all(12),
            child: QuillEditor(
              controller: _controller,
              scrollController: _scrollController,
              focusNode: _focusNode,
              config: const QuillEditorConfig(
                showCursor: false,
                autoFocus: false,
              ),
            ),
          ),
        ],
      ),
    );
  }
}
