import 'package:test/test.dart';
import 'package:openapi_story/openapi.dart';

/// tests for StoriesNearYouApi
void main() {
  final instance = Openapi().getStoriesNearYouApi();

  group(StoriesNearYouApi, () {
    // Get Stories near you for a member
    //
    // Returns the Stories near you feed for the member identified by `memberId`. The feed aggregates stories published by that member, by members in their trusted circle, and by members in their connected circle, ordered by publication time descending (newest first). When a requesting member browses another member's feed, stories are filtered by their Shared With level: only stories visible to the requesting member are included. Stories the requesting member is not permitted to see are silently excluded.
    //
    //Future<StoriesNearYouPage> getStoriesNearYou(String memberId, { int limit, DateTime before }) async
    test('test getStoriesNearYou', () async {
      // TODO
    });
  });
}
