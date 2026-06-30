import 'story.dart';

/// Port interface for retrieving the authenticated user's Stories near me feed.
///
/// This is a driving port (required interface) that defines the contract
/// for fetching paginated stories from the backend.
abstract interface class StoriesServicePort {
  /// Retrieves a page of stories near me, ordered newest first.
  ///
  /// Returns a list of [Story] objects or an empty list if no stories are found.
  /// Throws an exception if the retrieval fails.
  Future<List<Story>> getStoriesNearMe({
    required int offset,
    required int count,
  });
}
