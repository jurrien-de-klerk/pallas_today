import 'package:test/test.dart';
import 'package:openapi_story/openapi.dart';

/// tests for StoriesApi
void main() {
  final instance = Openapi().getStoriesApi();

  group(StoriesApi, () {
    // Create a new story
    //
    // Create a new story with the provided content
    //
    //Future<Story> createStory(StoryInput storyInput) async
    test('test createStory', () async {
      // TODO
    });

    // Delete a story
    //
    // Delete a specific story by its unique identifier
    //
    //Future deleteStory(String id) async
    test('test deleteStory', () async {
      // TODO
    });

    // Get a story by ID
    //
    // Retrieve a specific story by its unique identifier
    //
    //Future<Story> getStory(String id) async
    test('test getStory', () async {
      // TODO
    });

    // List all stories
    //
    // Retrieve a list of all stories
    //
    //Future<ListStories200Response> listStories({ int limit, int offset }) async
    test('test listStories', () async {
      // TODO
    });

    // Update a story
    //
    // Update an existing story with new content
    //
    //Future<Story> updateStory(String id, StoryInput storyInput) async
    test('test updateStory', () async {
      // TODO
    });
  });
}
