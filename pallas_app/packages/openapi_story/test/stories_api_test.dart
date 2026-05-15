import 'package:test/test.dart';
import 'package:openapi_story/openapi_story.dart';

/// tests for StoriesApi
void main() {
  final instance = OpenapiStory().getStoriesApi();

  group(StoriesApi, () {
    // Publish a story
    //
    // Create a new story for the authenticated member. The caller must supply the story content as a JSON Delta string and choose a Shared With level. Defaults to TRUSTED when no level is provided.
    //
    //Future<Story> createStory(StoryInput storyInput) async
    test('test createStory', () async {
      // TODO
    });

    // Delete a story
    //
    // Delete a story and all images associated with it. Only the author of the story may delete it. If the story exists but the requesting member is not the author, a 404 is returned so as not to reveal the story's existence to non-authors.
    //
    //Future deleteStory(String storyId) async
    test('test deleteStory', () async {
      // TODO
    });

    // Get a story
    //
    // Retrieve a specific story by its ID. The story is only returned when the requesting member's relationship with the author satisfies the story's Shared With level. If the story exists but the member is not permitted to see it, a 404 is returned so as not to reveal its existence.
    //
    //Future<Story> getStory(String storyId) async
    test('test getStory', () async {
      // TODO
    });

    // Update a story
    //
    // Update the content or Shared With level of an existing story. Only the author of the story may update it. If the story exists but the requesting member is not the author, a 404 is returned so as not to reveal the story's existence to non-authors.
    //
    //Future<Story> updateStory(String storyId, StoryInput storyInput) async
    test('test updateStory', () async {
      // TODO
    });
  });
}
