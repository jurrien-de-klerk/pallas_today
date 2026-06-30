import 'package:built_collection/built_collection.dart';

/// Represents a story in the domain layer.
class Story {
  /// UUID string of the member who authored the story.
  final String authorId;

  /// The story content as a list of Quill Delta JSON operations.
  final BuiltList<Map<String, dynamic>> content;

  const Story({required this.authorId, required this.content});

  Story copyWith({String? authorId, BuiltList<Map<String, dynamic>>? content}) {
    return Story(
      authorId: authorId ?? this.authorId,
      content: content ?? this.content,
    );
  }

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is Story &&
          runtimeType == other.runtimeType &&
          authorId == other.authorId &&
          content == other.content;

  @override
  int get hashCode => Object.hash(authorId, content);

  @override
  String toString() => 'Story(authorId: $authorId, content: $content)';
}
