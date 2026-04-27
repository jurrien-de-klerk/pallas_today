import 'dart:convert';

import 'package:dio/dio.dart';
import 'package:flutter_quill/flutter_quill.dart';
import 'package:openapi/openapi.dart';

import '../config/api_config.dart';

/// Application service that bridges the presentation layer and the proxy layer.
/// Transforms presentation data into proxy calls and translates proxy results
/// back into application-level outcomes.
class StoryService {
  StoryService({StoriesApi? api})
    : _api =
          api ??
          StoriesApi(
            Dio(BaseOptions(baseUrl: storyServiceBaseUrl)),
            standardSerializers,
          );

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
      return true;
    } on DioException catch (e) {
      // ignore: avoid_print
      print(
        '[StoryService] publishStory failed: ${e.message} '
        '(status ${e.response?.statusCode}) — ${e.response?.data}',
      );
      return false;
    } catch (e, st) {
      // ignore: avoid_print
      print('[StoryService] publishStory unexpected error: $e\n$st');
      return false;
    }
  }

  /// Fetches all stories from the StoryService microservice.
  ///
  /// Each story's Delta JSON string is deserialised back into a [Document].
  /// Returns an empty list on failure.
  Future<List<Document>> listStories() async {
    try {
      final response = await _api.listStories();
      final stories = response.data?.stories?.toList() ?? <Story>[];
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
      return [];
    }
  }
}
