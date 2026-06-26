// Openapi Generator last run: : 2026-06-26T10:44:31.196187
import 'package:openapi_generator_annotations/openapi_generator_annotations.dart';

@Openapi(
  inputSpec: InputSpec(path: '../api-specs/story-service/openapi.yaml'),
  generatorName: Generator.dio,
  outputDirectory: 'packages/openapi_story',
  additionalProperties: AdditionalProperties(pubName: 'openapi_story'),
)
class ApiClient {}
