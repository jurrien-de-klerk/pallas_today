import 'package:built_collection/built_collection.dart';
import 'package:built_value/json_object.dart';
import 'package:dio/dio.dart';
import 'package:openapi_story/openapi_story.dart' as story_api;

import '../../application/auth_service.dart';
import '../../config/api_config.dart';
import '../../domain/stories_service_port.dart';
import '../../domain/story.dart';

/// Adapter that implements [StoriesServicePort] via the generated Story API.
class StoriesServiceAdapter implements StoriesServicePort {
  StoriesServiceAdapter({story_api.StoriesNearMeApi? storiesNearMeApi})
    : _storiesNearMeApi =
          storiesNearMeApi ?? _buildOpenapiClient().getStoriesNearMeApi();

  final story_api.StoriesNearMeApi _storiesNearMeApi;

  @override
  Future<List<Story>> getStoriesNearMe({
    required int offset,
    required int count,
  }) async {
    final response = await _storiesNearMeApi.getStoriesNearMe(
      offset: offset,
      count: count,
    );

    final stories = response.data?.stories;
    if (stories == null || stories.isEmpty) {
      return const [];
    }

    return stories
        .map(
          (story) => Story(
            authorId: story.authorId,
            content: _mapStoryContent(story.content),
          ),
        )
        .toList(growable: false);
  }

  static story_api.OpenapiStory _buildOpenapiClient() {
    return story_api.OpenapiStory(
      basePathOverride: storyServiceBaseUrl,
      interceptors: [
        InterceptorsWrapper(
          onRequest: (options, handler) async {
            await AuthService.instance.refreshIfNeeded();
            final token = AuthService.instance.accessToken;
            if (token != null) {
              options.headers['Authorization'] = 'Bearer $token';
            }
            handler.next(options);
          },
        ),
      ],
    );
  }

  static BuiltList<Map<String, dynamic>> _mapStoryContent(BuiltList content) {
    final mapped = content
        .map<Map<String, dynamic>>((operation) {
          if (operation is JsonObject && operation.value is Map) {
            return Map<String, dynamic>.from(operation.value as Map);
          }
          if (operation is Map) {
            return Map<String, dynamic>.from(operation);
          }
          throw StateError('Unexpected story content operation type.');
        })
        .toList(growable: false);

    return BuiltList<Map<String, dynamic>>(mapped);
  }
}
