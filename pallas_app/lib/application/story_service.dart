import 'dart:convert';

import 'package:dio/dio.dart';
import 'package:flutter_quill/flutter_quill.dart';
import 'package:openapi_story/openapi.dart';
import 'package:pallas_logger/pallas_logger.dart';

import '../config/api_config.dart';
import 'auth_service.dart';

final _log = PallasLogger('StoryService');

/// Application service that bridges the presentation layer and the proxy layer.
/// Transforms presentation data into proxy calls and translates proxy results
/// back into application-level outcomes.
class StoryService {
  StoryService({StoriesApi? api}) : _api = api ?? _buildApi();

  static StoriesApi _buildApi() {
    return OpenapiStory(
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
    ).getStoriesApi();
  }

  final StoriesApi _api;

  /// Publishes [document] to the StoryService microservice.
  ///
  /// The document is serialised as a Quill Delta JSON string before sending.
  /// Returns `true` on success, `false` when the document is empty or the
  /// request fails.
  Future<bool> publishStory(Document document) async {
    if (document.toPlainText().trim().isEmpty) return false;
    try {
      final deltaJson = jsonEncode(document.toDelta().toJson());
      final input = StoryInput((b) => b..content = deltaJson);
      await _api.createStory(storyInput: input);
      _log.info('Story published');
      return true;
    } on DioException catch (e) {
      _log.warn(
        'publishStory failed: ${e.type} (status ${e.response?.statusCode})',
      );
      return false;
    } catch (e, st) {
      _log.fatal('publishStory unexpected error', e, st);
      _log.backtrace();
      return false;
    }
  }

  /// Fetches all stories from the StoryService microservice.
  ///
  /// Returns an empty list — the list endpoint has been removed from the
  /// StoryService spec. Use [getStoriesNearYou] once it is implemented.
  // TODO: replace with getStoriesNearYou once the backend is implemented.
  Future<List<Document>> listStories() async {
    _log.warn('listStories is not available: endpoint removed from spec');
    return [];
  }
}
