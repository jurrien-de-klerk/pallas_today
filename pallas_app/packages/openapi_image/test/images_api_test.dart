import 'package:test/test.dart';
import 'package:openapi_image/openapi_image.dart';

/// tests for ImagesApi
void main() {
  final instance = OpenapiImage().getImagesApi();

  group(ImagesApi, () {
    // Upload an image for a story
    //
    // Upload a new image and associate it with the specified story.
    //
    //Future<Image> addImage(String storyId, MultipartFile file) async
    test('test addImage', () async {
      // TODO
    });

    // Get an image
    //
    // Download the image file belonging to the specified story.
    //
    //Future<Uint8List> getImage(String storyId, String imageId) async
    test('test getImage', () async {
      // TODO
    });

    // Remove an image
    //
    // Delete an image belonging to the specified story from file storage.
    //
    //Future removeImage(String storyId, String imageId) async
    test('test removeImage', () async {
      // TODO
    });
  });
}
