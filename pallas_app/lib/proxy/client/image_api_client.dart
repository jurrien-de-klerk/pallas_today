// Openapi Generator last run: : 2026-05-15T20:07:11.282095
import 'package:openapi_generator_annotations/openapi_generator_annotations.dart';

@Openapi(
  inputSpec: InputSpec(path: '../api-specs/image-service/openapi.yaml'),
  generatorName: Generator.dio,
  outputDirectory: 'packages/openapi_image',
  additionalProperties: AdditionalProperties(pubName: 'openapi_image'),
)
class ImageApiClient {}
