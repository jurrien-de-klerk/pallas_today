import 'package:dio/dio.dart';
import 'package:flutter_quill/flutter_quill.dart';
import 'package:openapi_story/openapi_story.dart';
import 'package:pallas_logger/pallas_logger.dart';

import '../config/api_config.dart';
import 'auth_service.dart';

final _log = PallasLogger('StoryService');

/// Application service that bridges the presentation layer and the proxy layer.
/// Transforms presentation data into proxy calls and translates proxy results
/// back into application-level outcomes.
class StoryService {
  StoryService({StoriesNearMeApi? storiesNearMeApi})
    : _storiesNearMeApi =
          storiesNearMeApi ?? _buildOpenapiStory().getStoriesNearMeApi();

  static OpenapiStory _buildOpenapiStory() {
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
    );
  }

  final StoriesNearMeApi _storiesNearMeApi;

  /// Publishes [document] to the StoryService microservice.
  ///
  /// The document is serialised as a Quill Delta JSON string before sending.
  /// Returns `true` on success, `false` when the document is empty or the
  /// request fails.
  Future<bool> publishStory(Document document) async {
    _log.warn("publish story is disabled for now, as it needs refactoring.");
    return false;
    // if (document.toPlainText().trim().isEmpty) return false;
    // try {
    //   final deltaJson = document.toDelta().toJson();
    //   final input = StoryInput((b) => b..content = deltaJson);
    //   await _api.createStory(storyInput: input);
    //   _log.info('Story published');
    //   return true;
    // } on DioException catch (e) {
    //   _log.warn(
    //     'publishStory failed: ${e.type} (status ${e.response?.statusCode})',
    //   );
    //   return false;
    // } catch (e, st) {
    //   _log.error('publishStory unexpected error', e, st);
    //   _log.backtrace();
    //   return false;
    // }
  }

  /// Fetches all stories from the StoryService microservice.
  ///
  /// Retrieves the authenticated user's Stories near me feed and converts
  /// each story's Quill Delta content into a Flutter Document.
  /// Returns an empty list on error.
  Future<List<Document>> listStories() async {
    try {
      _log.info("Fetching stories near me...");
      final response = await _storiesNearMeApi.getStoriesNearMe();
      final storiesPage = response.data;

      if (storiesPage?.stories == null || storiesPage!.stories.isEmpty) {
        _log.info('No stories found in Stories near me feed');
        return [];
      }

      final documents = <Document>[];
      for (final story in storiesPage.stories) {
        try {
          final deltaJson = story.content
              .map((op) {
                try {
                  return (op as dynamic).value;
                } catch (_) {
                  return op;
                }
              })
              .cast<Map<String, dynamic>>()
              .toList();

          // Quill Delta documents must end with an insert op whose string ends
          // with '\n'. Guard against stories stored without a trailing newline.
          final lastInsert = deltaJson.isEmpty
              ? null
              : deltaJson.last['insert'];
          if (lastInsert is! String || !lastInsert.endsWith('\n')) {
            deltaJson.add({'insert': '\n'});
          }

          final document = Document.fromJson(deltaJson);
          documents.add(document);
        } catch (e) {
          _log.warn(
            'Failed to convert story content to document, reason: ${e.toString()}',
          );
          // Continue with other stories
        }
      }

      _log.info('Retrieved ${documents.length} stories');
      return documents;
    } on DioException catch (e) {
      _log.error(
        "Failed to retrieve stories near me. Reason: ${e.message} (error: ${e.error})",
      );
      _log.backtrace();
      return [];
    } catch (e, st) {
      _log.error('listStories unexpected error', e, st);
      _log.backtrace();
      return [];
    }
  }
}
