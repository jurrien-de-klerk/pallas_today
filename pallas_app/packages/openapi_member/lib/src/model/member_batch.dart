//
// AUTO-GENERATED FILE, DO NOT MODIFY!
//

// ignore_for_file: unused_element
import 'package:openapi_member/src/model/member.dart';
import 'package:built_collection/built_collection.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';

part 'member_batch.g.dart';

/// MemberBatch
///
/// Properties:
/// * [members] - The members found for the requested user IDs. Members not found for a given ID are omitted.
@BuiltValue()
abstract class MemberBatch implements Built<MemberBatch, MemberBatchBuilder> {
  /// The members found for the requested user IDs. Members not found for a given ID are omitted.
  @BuiltValueField(wireName: r'members')
  BuiltList<Member> get members;

  MemberBatch._();

  factory MemberBatch([void updates(MemberBatchBuilder b)]) = _$MemberBatch;

  @BuiltValueHook(initializeBuilder: true)
  static void _defaults(MemberBatchBuilder b) => b;

  @BuiltValueSerializer(custom: true)
  static Serializer<MemberBatch> get serializer => _$MemberBatchSerializer();
}

class _$MemberBatchSerializer implements PrimitiveSerializer<MemberBatch> {
  @override
  final Iterable<Type> types = const [MemberBatch, _$MemberBatch];

  @override
  final String wireName = r'MemberBatch';

  Iterable<Object?> _serializeProperties(
    Serializers serializers,
    MemberBatch object, {
    FullType specifiedType = FullType.unspecified,
  }) sync* {
    yield r'members';
    yield serializers.serialize(
      object.members,
      specifiedType: const FullType(BuiltList, [FullType(Member)]),
    );
  }

  @override
  Object serialize(
    Serializers serializers,
    MemberBatch object, {
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
    required MemberBatchBuilder result,
    required List<Object?> unhandled,
  }) {
    for (var i = 0; i < serializedList.length; i += 2) {
      final key = serializedList[i] as String;
      final value = serializedList[i + 1];
      switch (key) {
        case r'members':
          final valueDes = serializers.deserialize(
            value,
            specifiedType: const FullType(BuiltList, [FullType(Member)]),
          ) as BuiltList<Member>;
          result.members.replace(valueDes);
          break;
        default:
          unhandled.add(key);
          unhandled.add(value);
          break;
      }
    }
  }

  @override
  MemberBatch deserialize(
    Serializers serializers,
    Object serialized, {
    FullType specifiedType = FullType.unspecified,
  }) {
    final result = MemberBatchBuilder();
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
