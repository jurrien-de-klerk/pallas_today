// Openapi Generator last run: : 2026-05-22T19:20:38.818270
import 'package:openapi_generator_annotations/openapi_generator_annotations.dart';

@Openapi(
  inputSpec: InputSpec(path: '../api-specs/member-service/openapi.yaml'),
  generatorName: Generator.dio,
  outputDirectory: 'packages/openapi_member',
  additionalProperties: AdditionalProperties(pubName: 'openapi_member'),
)
class MemberApiClient {}
