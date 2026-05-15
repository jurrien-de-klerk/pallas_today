import 'package:test/test.dart';
import 'package:openapi_story/openapi_story.dart';

// tests for Story
void main() {
  final instance = StoryBuilder();
  // TODO add properties to the builder and call build()

  group(Story, () {
    // Unique identifier of the story
    // String id
    test('to test the property `id`', () async {
      // TODO
    });

    // The userId of the member who published the story
    // String authorId
    test('to test the property `authorId`', () async {
      // TODO
    });

    // The story body as a JSON-serialised Quill Delta array of insert operations. Image references within the Delta are URLs pointing to file storage; raw image data is never embedded.
    // String content
    test('to test the property `content`', () async {
      // TODO
    });

    // SharedWith sharedWith
    test('to test the property `sharedWith`', () async {
      // TODO
    });

    // The date and time at which the story was published (ISO 8601, UTC). Used to order stories in the Stories near you feed.
    // DateTime publishedAt
    test('to test the property `publishedAt`', () async {
      // TODO
    });
  });
}
