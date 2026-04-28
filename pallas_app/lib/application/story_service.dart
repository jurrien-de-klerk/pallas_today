import 'dart:convert';

import 'package:dio/dio.dart';
import 'package:flutter_quill/flutter_quill.dart';
import 'package:openapi/openapi.dart';
import 'package:pallas_logger/pallas_logger.dart';

import '../config/api_config.dart';

final _log = PallasLogger('StoryService');

/// Application service that bridges the presentation layer and the proxy layer.
/// Transforms presentation data into proxy calls and translates proxy results
/// back into application-level outcomes.
class StoryService {
  StoryService({StoriesApi? api})
    : _api =
          api ?? Openapi(basePathOverride: storyServiceBaseUrl).getStoriesApi();

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
      final input = StoryInputBuilder()..story = deltaJson;
      await _api.createStory(storyInput: input.build());
      _log.info('Story published');
      return true;
    } on DioException catch (e) {
      _log.warn(
        'publishStory failed: ${e.message} (status ${e.response?.statusCode})',
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
  /// Each story's Delta JSON string is deserialised back into a [Document].
  /// Returns an empty list on failure.
  Future<List<Document>> listStories() async {
    _log.info('Fetching stories');
    try {
      final response = await _api.listStories();
      final stories = response.data?.stories?.toList() ?? <Story>[];
      _log.debug('Received ${stories.length} stories');
      return stories.map((s) {
        try {
          final ops = jsonDecode(s.story) as List<dynamic>;
          return Document.fromJson(ops);
        } catch (_) {
          // Fall back to plain text when the stored value is not Delta JSON.
          return Document.fromJson([
            {'insert': '${s.story}\n'},
          ]);
        }
      }).toList();
    } catch (_) {
      _log.warn('listStories failed');
      return [];
    }
  }
}
