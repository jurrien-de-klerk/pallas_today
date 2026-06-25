// Openapi Generator last run: : 2026-06-24T23:00:03.655761
import 'package:openapi_generator_annotations/openapi_generator_annotations.dart';

@Openapi(
  inputSpec: InputSpec(path: '../api-specs/story-service/openapi.yaml'),
  generatorName: Generator.dio,
  outputDirectory: 'packages/openapi_story',
  additionalProperties: AdditionalProperties(pubName: 'openapi_story'),
)
class ApiClient {}
