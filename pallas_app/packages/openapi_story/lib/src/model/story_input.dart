//
// AUTO-GENERATED FILE, DO NOT MODIFY!
//

// ignore_for_file: unused_element
import 'package:openapi_story/src/model/shared_with.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';

part 'story_input.g.dart';

/// StoryInput
///
/// Properties:
/// * [content] - The story body as a JSON-serialised Quill Delta array of insert operations. Image references within the Delta must be URLs pointing to file storage; inline base64 image data is not permitted.
/// * [sharedWith]
@BuiltValue()
abstract class StoryInput implements Built<StoryInput, StoryInputBuilder> {
  /// The story body as a JSON-serialised Quill Delta array of insert operations. Image references within the Delta must be URLs pointing to file storage; inline base64 image data is not permitted.
  @BuiltValueField(wireName: r'content')
  String get content;

  @BuiltValueField(wireName: r'sharedWith')
  SharedWith? get sharedWith;
  // enum sharedWithEnum {  TRUSTED,  CONNECTED,  COMMUNITY,  PUBLIC,  };

  StoryInput._();

  factory StoryInput([void updates(StoryInputBuilder b)]) = _$StoryInput;

  @BuiltValueHook(initializeBuilder: true)
  static void _defaults(StoryInputBuilder b) => b;

  @BuiltValueSerializer(custom: true)
  static Serializer<StoryInput> get serializer => _$StoryInputSerializer();
}

class _$StoryInputSerializer implements PrimitiveSerializer<StoryInput> {
  @override
  final Iterable<Type> types = const [StoryInput, _$StoryInput];

  @override
  final String wireName = r'StoryInput';

  Iterable<Object?> _serializeProperties(
    Serializers serializers,
    StoryInput object, {
    FullType specifiedType = FullType.unspecified,
  }) sync* {
    yield r'content';
    yield serializers.serialize(
      object.content,
      specifiedType: const FullType(String),
    );
    if (object.sharedWith != null) {
      yield r'sharedWith';
      yield serializers.serialize(
        object.sharedWith,
        specifiedType: const FullType(SharedWith),
      );
    }
  }

  @override
  Object serialize(
    Serializers serializers,
    StoryInput object, {
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
    required StoryInputBuilder result,
    required List<Object?> unhandled,
  }) {
    for (var i = 0; i < serializedList.length; i += 2) {
      final key = serializedList[i] as String;
      final value = serializedList[i + 1];
      switch (key) {
        case r'content':
          final valueDes = serializers.deserialize(
            value,
            specifiedType: const FullType(String),
          ) as String;
          result.content = valueDes;
          break;
        case r'sharedWith':
          final valueDes = serializers.deserialize(
            value,
            specifiedType: const FullType(SharedWith),
          ) as SharedWith;
          result.sharedWith = valueDes;
          break;
        default:
          unhandled.add(key);
          unhandled.add(value);
          break;
      }
    }
  }

  @override
  StoryInput deserialize(
    Serializers serializers,
    Object serialized, {
    FullType specifiedType = FullType.unspecified,
  }) {
    final result = StoryInputBuilder();
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
