import 'stories_service_port.dart';
import 'story.dart';

/// Domain repository for paginated story retrieval.
abstract interface class StoryRepository {
  /// Fetches stories in the requested range.
  Future<List<Story>> fetchStories({required int offset, required int count});

  /// Drops all cached stories.
  void clearCache();
}

/// Story repository that caches pages by absolute feed index.
class CachedStoryRepository implements StoryRepository {
  CachedStoryRepository({required StoriesServicePort storiesServicePort})
    : _storiesServicePort = storiesServicePort;

  final StoriesServicePort _storiesServicePort;
  final List<Story> _cacheByOffset = <Story>[];

  @override
  Future<List<Story>> fetchStories({
    required int offset,
    required int count,
  }) async {
    if (offset < 0) {
      throw ArgumentError.value(offset, 'offset', 'must be >= 0');
    }
    if (count < 0) {
      throw ArgumentError.value(count, 'count', 'must be >= 0');
    }
    if (count == 0) {
      return const <Story>[];
    }

    await _fillMissingRanges(offset: offset, count: count);
    return _readCachedRange(offset: offset, count: count);
  }

  @override
  void clearCache() {
    _cacheByOffset.clear();
  }

  Future<void> _fillMissingRanges({
    required int offset,
    required int count,
  }) async {
    if (offset + count <= _cacheByOffset.length) {
      return;
    }

    int numToFetch = (offset + count) - _cacheByOffset.length;
    final stories = await _storiesServicePort.getStoriesNearMe(
      offset: _cacheByOffset.length,
      count: numToFetch,
    );
    _cacheByOffset.addAll(stories);
  }

  List<Story> _readCachedRange({required int offset, required int count}) {
    if (_cacheByOffset.length < offset) {
      return [];
    }

    final end = (offset + count).clamp(0, _cacheByOffset.length);
    return _cacheByOffset.sublist(offset, end);
  }
}
