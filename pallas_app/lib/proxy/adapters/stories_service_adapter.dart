import 'package:built_collection/built_collection.dart';
import 'package:built_value/json_object.dart';
import 'package:dio/dio.dart';
import 'package:openapi_story/openapi_story.dart' as story_api;
import 'package:pallas_logger/pallas_logger.dart';

import '../../application/auth_service.dart';
import '../../config/api_config.dart';
import '../../domain/stories_service_port.dart';
import '../../domain/story.dart';

final _log = PallasLogger('StoriesServiceAdapter');

/// Adapter that implements [StoriesServicePort] via the generated Story API.
class StoriesServiceAdapter implements StoriesServicePort {
  StoriesServiceAdapter({story_api.StoriesNearMeApi? storiesNearMeApi})
    : _storiesNearMeApi =
          storiesNearMeApi ?? _buildOpenapiClient().getStoriesNearMeApi() {
    _log.debug(
      'StoriesServiceAdapter initialized. baseUrl=$storyServiceBaseUrl',
    );
  }

  final story_api.StoriesNearMeApi _storiesNearMeApi;

  @override
  Future<List<Story>> getStoriesNearMe({
    required int offset,
    required int count,
  }) async {
    _log.debug('Calling Stories API. offset=$offset, count=$count');
    try {
      final response = await _storiesNearMeApi.getStoriesNearMe(
        offset: offset,
        count: count,
      );

      _log.debug(
        'Stories API response received. statusCode=${response.statusCode}',
      );

      final stories = response.data?.stories;
      if (stories == null || stories.isEmpty) {
        _log.debug('Stories API returned no stories.');
        return const [];
      }

      final mappedStories = stories
          .map(
            (story) => Story(
              authorId: story.authorId,
              content: _mapStoryContent(story.content),
            ),
          )
          .toList(growable: false);
      _log.debug(
        'Stories API call completed. returnedStoryCount=${mappedStories.length}',
      );
      return mappedStories;
    } on DioException catch (error, stackTrace) {
      final statusCode = error.response?.statusCode;
      final hasResponseBody = error.response?.data != null;
      final requestPath = error.requestOptions.path;
      final requestMethod = error.requestOptions.method;
      final fullUri = error.requestOptions.uri.toString();
      _log.error('Stories API request failed', error, stackTrace);
      _log.debug(
        'Stories API failure details. '
        'requestMethod=$requestMethod, requestPath=$requestPath, fullUri=$fullUri, '
        'dioType=${error.type}, statusCode=$statusCode, hasResponseBody=$hasResponseBody',
      );
      rethrow;
    }
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
          _log.debug(
            'Unexpected story content operation type encountered. type=${operation.runtimeType}',
          );
          throw StateError('Unexpected story content operation type.');
        })
        .toList(growable: false);

    return BuiltList<Map<String, dynamic>>(mapped);
  }
}
