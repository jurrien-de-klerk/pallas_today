import 'package:test/test.dart';
import 'package:openapi_story/openapi_story.dart';

// tests for StoryInput
void main() {
  final instance = StoryInputBuilder();
  // TODO add properties to the builder and call build()

  group(StoryInput, () {
    // The story body as a JSON-serialised Quill Delta array of insert operations. Image references within the Delta must be URLs pointing to file storage; inline base64 image data is not permitted.
    // String content
    test('to test the property `content`', () async {
      // TODO
    });

    // SharedWith sharedWith
    test('to test the property `sharedWith`', () async {
      // TODO
    });
  });
}
