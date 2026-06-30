import 'package:pallas_logger/pallas_logger.dart';

import 'stories_service_port.dart';
import 'story.dart';

final _log = PallasLogger('CachedStoryRepository');

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
    _log.debug('fetchStories called. offset=$offset, count=$count');
    if (offset < 0) {
      throw ArgumentError.value(offset, 'offset', 'must be >= 0');
    }
    if (count < 0) {
      throw ArgumentError.value(count, 'count', 'must be >= 0');
    }
    if (count == 0) {
      _log.debug('fetchStories returns early for count=0.');
      return const <Story>[];
    }

    await _fillMissingRanges(offset: offset, count: count);
    final result = _readCachedRange(offset: offset, count: count);
    _log.debug(
      'fetchStories completed. returnedCount=${result.length}, cacheSize=${_cacheByOffset.length}',
    );
    return result;
  }

  @override
  void clearCache() {
    _log.debug('clearCache called. previousCacheSize=${_cacheByOffset.length}');
    _cacheByOffset.clear();
  }

  Future<void> _fillMissingRanges({
    required int offset,
    required int count,
  }) async {
    if (offset + count <= _cacheByOffset.length) {
      _log.debug(
        'Cache already satisfies requested range. cacheSize=${_cacheByOffset.length}',
      );
      return;
    }

    int numToFetch = (offset + count) - _cacheByOffset.length;
    _log.debug(
      'Fetching missing stories from port. fetchOffset=${_cacheByOffset.length}, fetchCount=$numToFetch',
    );
    final stories = await _storiesServicePort.getStoriesNearMe(
      offset: _cacheByOffset.length,
      count: numToFetch,
    );
    _cacheByOffset.addAll(stories);
    _log.debug(
      'Missing range fetch complete. fetchedCount=${stories.length}, cacheSize=${_cacheByOffset.length}',
    );
  }

  List<Story> _readCachedRange({required int offset, required int count}) {
    if (_cacheByOffset.length < offset) {
      _log.debug(
        'Requested offset is beyond cache end. offset=$offset, cacheSize=${_cacheByOffset.length}',
      );
      return [];
    }

    final end = offset + count <= _cacheByOffset.length
        ? offset + count
        : _cacheByOffset.length;
    return _cacheByOffset.sublist(offset, end);
  }
}
