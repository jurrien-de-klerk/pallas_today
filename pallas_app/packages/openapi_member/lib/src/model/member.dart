//
// AUTO-GENERATED FILE, DO NOT MODIFY!
//

// ignore_for_file: unused_element
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';

part 'member.g.dart';

/// Member
///
/// Properties:
/// * [userId] - The unique identifier of the member. Corresponds to the OIDC `sub` claim issued by Keycloak.
/// * [firstName] - The member's given name.
/// * [lastName] - The member's family name.
@BuiltValue()
abstract class Member implements Built<Member, MemberBuilder> {
  /// The unique identifier of the member. Corresponds to the OIDC `sub` claim issued by Keycloak.
  @BuiltValueField(wireName: r'userId')
  String get userId;

  /// The member's given name.
  @BuiltValueField(wireName: r'firstName')
  String get firstName;

  /// The member's family name.
  @BuiltValueField(wireName: r'lastName')
  String get lastName;

  Member._();

  factory Member([void updates(MemberBuilder b)]) = _$Member;

  @BuiltValueHook(initializeBuilder: true)
  static void _defaults(MemberBuilder b) => b;

  @BuiltValueSerializer(custom: true)
  static Serializer<Member> get serializer => _$MemberSerializer();
}

class _$MemberSerializer implements PrimitiveSerializer<Member> {
  @override
  final Iterable<Type> types = const [Member, _$Member];

  @override
  final String wireName = r'Member';

  Iterable<Object?> _serializeProperties(
    Serializers serializers,
    Member object, {
    FullType specifiedType = FullType.unspecified,
  }) sync* {
    yield r'userId';
    yield serializers.serialize(
      object.userId,
      specifiedType: const FullType(String),
    );
    yield r'firstName';
    yield serializers.serialize(
      object.firstName,
      specifiedType: const FullType(String),
    );
    yield r'lastName';
    yield serializers.serialize(
      object.lastName,
      specifiedType: const FullType(String),
    );
  }

  @override
  Object serialize(
    Serializers serializers,
    Member object, {
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
    required MemberBuilder result,
    required List<Object?> unhandled,
  }) {
    for (var i = 0; i < serializedList.length; i += 2) {
      final key = serializedList[i] as String;
      final value = serializedList[i + 1];
      switch (key) {
        case r'userId':
          final valueDes = serializers.deserialize(
            value,
            specifiedType: const FullType(String),
          ) as String;
          result.userId = valueDes;
          break;
        case r'firstName':
          final valueDes = serializers.deserialize(
            value,
            specifiedType: const FullType(String),
          ) as String;
          result.firstName = valueDes;
          break;
        case r'lastName':
          final valueDes = serializers.deserialize(
            value,
            specifiedType: const FullType(String),
          ) as String;
          result.lastName = valueDes;
          break;
        default:
          unhandled.add(key);
          unhandled.add(value);
          break;
      }
    }
  }

  @override
  Member deserialize(
    Serializers serializers,
    Object serialized, {
    FullType specifiedType = FullType.unspecified,
  }) {
    final result = MemberBuilder();
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
