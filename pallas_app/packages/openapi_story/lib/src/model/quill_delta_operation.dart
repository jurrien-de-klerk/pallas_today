//
// AUTO-GENERATED FILE, DO NOT MODIFY!
//

// ignore_for_file: unused_element
import 'package:built_collection/built_collection.dart';
import 'package:openapi_story/src/model/quill_delta_operation_insert.dart';
import 'package:built_value/json_object.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';

part 'quill_delta_operation.g.dart';

/// A single Quill Delta operation. Each operation represents an insert, format, or delete action. Most commonly, insert operations with optional attributes.
///
/// Properties:
/// * [insert]
/// * [attributes] - Optional formatting attributes applied to the inserted content (e.g., bold, italic, link, image URL).
@BuiltValue()
abstract class QuillDeltaOperation
    implements Built<QuillDeltaOperation, QuillDeltaOperationBuilder> {
  @BuiltValueField(wireName: r'insert')
  QuillDeltaOperationInsert get insert;

  /// Optional formatting attributes applied to the inserted content (e.g., bold, italic, link, image URL).
  @BuiltValueField(wireName: r'attributes')
  BuiltMap<String, JsonObject?>? get attributes;

  QuillDeltaOperation._();

  factory QuillDeltaOperation([void updates(QuillDeltaOperationBuilder b)]) =
      _$QuillDeltaOperation;

  @BuiltValueHook(initializeBuilder: true)
  static void _defaults(QuillDeltaOperationBuilder b) => b;

  @BuiltValueSerializer(custom: true)
  static Serializer<QuillDeltaOperation> get serializer =>
      _$QuillDeltaOperationSerializer();
}

class _$QuillDeltaOperationSerializer
    implements PrimitiveSerializer<QuillDeltaOperation> {
  @override
  final Iterable<Type> types = const [
    QuillDeltaOperation,
    _$QuillDeltaOperation
  ];

  @override
  final String wireName = r'QuillDeltaOperation';

  Iterable<Object?> _serializeProperties(
    Serializers serializers,
    QuillDeltaOperation object, {
    FullType specifiedType = FullType.unspecified,
  }) sync* {
    yield r'insert';
    yield serializers.serialize(
      object.insert,
      specifiedType: const FullType(QuillDeltaOperationInsert),
    );
    if (object.attributes != null) {
      yield r'attributes';
      yield serializers.serialize(
        object.attributes,
        specifiedType: const FullType(
            BuiltMap, [FullType(String), FullType.nullable(JsonObject)]),
      );
    }
  }

  @override
  Object serialize(
    Serializers serializers,
    QuillDeltaOperation object, {
    FullType specifiedType = FullType.unspecified,
  }) {
    return _serializeProperties(serializers, object,
            specifiedType: specifiedType)
        .toList();
  }

  void _deserializeProperties(
    Serializers serializers,
    Object serialized, {
    FullType specifiedType = FullType.unspecified,
    required List<Object?> serializedList,
    required QuillDeltaOperationBuilder result,
    required List<Object?> unhandled,
  }) {
    for (var i = 0; i < serializedList.length; i += 2) {
      final key = serializedList[i] as String;
      final value = serializedList[i + 1];
      switch (key) {
        case r'insert':
          final valueDes = serializers.deserialize(
            value,
            specifiedType: const FullType(QuillDeltaOperationInsert),
          ) as QuillDeltaOperationInsert;
          result.insert.replace(valueDes);
          break;
        case r'attributes':
          final valueDes = serializers.deserialize(
            value,
            specifiedType: const FullType(
                BuiltMap, [FullType(String), FullType.nullable(JsonObject)]),
          ) as BuiltMap<String, JsonObject?>;
          result.attributes.replace(valueDes);
          break;
        default:
          unhandled.add(key);
          unhandled.add(value);
          break;
      }
    }
  }

  @override
  QuillDeltaOperation deserialize(
    Serializers serializers,
    Object serialized, {
    FullType specifiedType = FullType.unspecified,
  }) {
    final result = QuillDeltaOperationBuilder();
    final serializedList = (serialized as Iterable<Object?>).toList();
    final unhandled = <Object?>[];
    _deserializeProperties(
      serializers,
      serialized,
      specifiedType: specifiedType,
      serializedList: serializedList,
      unhandled: unhandled,
      result: result,
    );
    return result.build();
  }
}
