import 'package:dio/dio.dart';
import 'package:openapi/openapi.dart';

/// Application service that bridges the presentation layer and the proxy layer.
/// Transforms presentation data into proxy calls and translates proxy results
/// back into application-level outcomes.
class StoryService {
  StoryService({StoriesApi? api})
    : _api =
          api ??
          StoriesApi(
            Dio(BaseOptions(baseUrl: 'http://localhost:8080')),
            serializers,
          );

  final StoriesApi _api;

  /// Publishes [storyText] to the StoryService microservice.
  ///
  /// Returns `true` on success, `false` on failure.
  Future<bool> publishStory(String storyText) async {
    final text = storyText.trim();
    if (text.isEmpty) return false;
    try {
      final input = StoryInputBuilder()..story = text;
      await _api.createStory(storyInput: input.build());
      return true;
    } catch (_) {
      return false;
    }
  }
}
