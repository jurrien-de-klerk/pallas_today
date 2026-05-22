// Openapi Generator last run: : 2026-05-22T19:45:32.724813
import 'package:openapi_generator_annotations/openapi_generator_annotations.dart';

@Openapi(
  inputSpec: InputSpec(path: '../api-specs/member-service/openapi.yaml'),
  generatorName: Generator.dio,
  outputDirectory: 'packages/openapi_member',
  additionalProperties: AdditionalProperties(pubName: 'openapi_member'),
)
class MemberApiClient {}
