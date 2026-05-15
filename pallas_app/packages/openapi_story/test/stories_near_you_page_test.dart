import 'package:test/test.dart';
import 'package:openapi_story/openapi.dart';

// tests for StoriesNearYouPage
void main() {
  final instance = StoriesNearYouPageBuilder();
  // TODO add properties to the builder and call build()

  group(StoriesNearYouPage, () {
    // Stories ordered by publishedAt descending. Only stories visible to the requesting member are included.
    // BuiltList<Story> stories
    test('to test the property `stories`', () async {
      // TODO
    });

    // Pass this value as the `before` query parameter to fetch the next page. Absent when there are no more stories to return.
    // DateTime nextBefore
    test('to test the property `nextBefore`', () async {
      // TODO
    });
  });
}
