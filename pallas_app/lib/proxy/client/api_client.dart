// Openapi Generator last run: : 2026-05-16T14:25:28.011435
import 'package:openapi_generator_annotations/openapi_generator_annotations.dart';

@Openapi(
  inputSpec: InputSpec(path: '../api-specs/story-service/openapi.yaml'),
  generatorName: Generator.dio,
  outputDirectory: 'packages/openapi_story',
  additionalProperties: AdditionalProperties(pubName: 'openapi_story'),
)
class ApiClient {}
