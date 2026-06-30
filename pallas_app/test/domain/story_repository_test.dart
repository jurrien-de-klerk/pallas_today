import 'package:built_collection/built_collection.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:pallas_app/domain/stories_service_port.dart';
import 'package:pallas_app/domain/story.dart';
import 'package:pallas_app/domain/story_repository.dart';

void main() {
  group('CachedStoryRepository', () {
    test(
      'returns cached stories without re-fetching requested range',
      () async {
        final port = _FakeStoriesServicePort(
          responsesByRange: {
            _Range(offset: 0, count: 3): [_story(0), _story(1), _story(2)],
          },
        );
        final repository = CachedStoryRepository(storiesServicePort: port);

        final firstPage = await repository.fetchStories(offset: 0, count: 3);
        final secondPage = await repository.fetchStories(offset: 1, count: 2);

        _expectStoryIndexes(firstPage, [0, 1, 2]);
        _expectStoryIndexes(secondPage, [1, 2]);
        expect(port.calls, [_Range(offset: 0, count: 3)]);
      },
    );

    test('fetches only missing stories for an expanded range', () async {
      final port = _FakeStoriesServicePort(
        responsesByRange: {
          _Range(offset: 0, count: 3): [_story(0), _story(1), _story(2)],
          _Range(offset: 3, count: 2): [_story(3), _story(4)],
        },
      );
      final repository = CachedStoryRepository(storiesServicePort: port);

      await repository.fetchStories(offset: 0, count: 3);
      final expandedPage = await repository.fetchStories(offset: 0, count: 5);

      _expectStoryIndexes(expandedPage, [0, 1, 2, 3, 4]);
      expect(port.calls, [
        _Range(offset: 0, count: 3),
        _Range(offset: 3, count: 2),
      ]);
    });

    test('remembers end-of-feed and skips calls beyond end', () async {
      final port = _FakeStoriesServicePort(
        responsesByRange: {
          _Range(offset: 0, count: 5): [_story(0), _story(1), _story(2)],
        },
      );
      final repository = CachedStoryRepository(storiesServicePort: port);

      final firstPage = await repository.fetchStories(offset: 0, count: 5);
      final beyondEnd = await repository.fetchStories(offset: 4, count: 2);

      _expectStoryIndexes(firstPage, [0, 1, 2]);
      expect(beyondEnd, isEmpty);
      expect(port.calls, [
        _Range(offset: 0, count: 5),
        _Range(offset: 3, count: 3),
      ]);
    });

    test('clearCache forces re-fetch', () async {
      final port = _FakeStoriesServicePort(
        responsesByRange: {
          _Range(offset: 0, count: 2): [_story(0), _story(1)],
        },
      );
      final repository = CachedStoryRepository(storiesServicePort: port);

      await repository.fetchStories(offset: 0, count: 2);
      repository.clearCache();
      await repository.fetchStories(offset: 0, count: 2);

      expect(port.calls, [
        _Range(offset: 0, count: 2),
        _Range(offset: 0, count: 2),
      ]);
    });
  });
}

void _expectStoryIndexes(List<Story> stories, List<int> indexes) {
  expect(stories.length, indexes.length);
  for (var i = 0; i < indexes.length; i += 1) {
    expect(stories[i].authorId, 'author-${indexes[i]}');
  }
}

Story _story(int index) {
  return Story(
    authorId: 'author-$index',
    content: BuiltList<Map<String, dynamic>>([
      {'insert': 'Story $index'},
      {'insert': '\n'},
    ]),
  );
}

class _FakeStoriesServicePort implements StoriesServicePort {
  _FakeStoriesServicePort({required this.responsesByRange});

  final Map<_Range, List<Story>> responsesByRange;
  final List<_Range> calls = <_Range>[];

  @override
  Future<List<Story>> getStoriesNearMe({
    required int offset,
    required int count,
  }) async {
    final range = _Range(offset: offset, count: count);
    calls.add(range);
    return responsesByRange[range] ?? const <Story>[];
  }
}

class _Range {
  const _Range({required this.offset, required this.count});

  final int offset;
  final int count;

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is _Range &&
          runtimeType == other.runtimeType &&
          offset == other.offset &&
          count == other.count;

  @override
  int get hashCode => Object.hash(offset, count);

  @override
  String toString() => '_Range(offset: $offset, count: $count)';
}
