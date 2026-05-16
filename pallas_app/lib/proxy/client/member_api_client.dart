// Openapi Generator last run: : 2026-05-16T20:43:59.091583
import 'package:openapi_generator_annotations/openapi_generator_annotations.dart';

@Openapi(
  inputSpec: InputSpec(path: '../api-specs/member-service/openapi.yaml'),
  generatorName: Generator.dio,
  outputDirectory: 'packages/openapi_member',
  additionalProperties: AdditionalProperties(pubName: 'openapi_member'),
)
class MemberApiClient {}
