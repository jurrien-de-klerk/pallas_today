//
// AUTO-GENERATED FILE, DO NOT MODIFY!
//

// ignore_for_file: unused_element
import 'dart:core';
import 'package:built_value/json_object.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';
import 'package:one_of/one_of.dart';

part 'quill_delta_operation_insert.g.dart';

/// The content to insert (text or embedded object reference)
@BuiltValue()
abstract class QuillDeltaOperationInsert
    implements
        Built<QuillDeltaOperationInsert, QuillDeltaOperationInsertBuilder> {
  /// One Of [JsonObject], [String]
  OneOf get oneOf;

  QuillDeltaOperationInsert._();

  factory QuillDeltaOperationInsert(
          [void updates(QuillDeltaOperationInsertBuilder b)]) =
      _$QuillDeltaOperationInsert;

  @BuiltValueHook(initializeBuilder: true)
  static void _defaults(QuillDeltaOperationInsertBuilder b) => b;

  @BuiltValueSerializer(custom: true)
  static Serializer<QuillDeltaOperationInsert> get serializer =>
      _$QuillDeltaOperationInsertSerializer();
}

class _$QuillDeltaOperationInsertSerializer
    implements PrimitiveSerializer<QuillDeltaOperationInsert> {
  @override
  final Iterable<Type> types = const [
    QuillDeltaOperationInsert,
    _$QuillDeltaOperationInsert
  ];

  @override
  final String wireName = r'QuillDeltaOperationInsert';

  Iterable<Object?> _serializeProperties(
    Serializers serializers,
    QuillDeltaOperationInsert object, {
    FullType specifiedType = FullType.unspecified,
  }) sync* {}

  @override
  Object serialize(
    Serializers serializers,
    QuillDeltaOperationInsert object, {
    FullType specifiedType = FullType.unspecified,
  }) {
    final oneOf = object.oneOf;
    return serializers.serialize(oneOf.value,
        specifiedType: FullType(oneOf.valueType))!;
  }

  @override
  QuillDeltaOperationInsert deserialize(
    Serializers serializers,
    Object serialized, {
    FullType specifiedType = FullType.unspecified,
  }) {
    final result = QuillDeltaOperationInsertBuilder();
    Object? oneOfDataSrc;
    final targetType = const FullType(OneOf, [
      FullType(String),
      FullType(JsonObject),
    ]);
    oneOfDataSrc = serialized;
    result.oneOf = serializers.deserialize(oneOfDataSrc,
        specifiedType: targetType) as OneOf;
    return result.build();
  }
}
