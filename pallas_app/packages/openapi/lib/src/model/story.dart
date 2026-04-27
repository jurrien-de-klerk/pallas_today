//
// AUTO-GENERATED FILE, DO NOT MODIFY!
//

// ignore_for_file: unused_element
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';

part 'story.g.dart';

/// Story
///
/// Properties:
/// * [id] - Unique identifier for the story
/// * [story] - The story content as a Quill Delta JSON string (JSON-serialised array of Delta ops).
@BuiltValue()
abstract class Story implements Built<Story, StoryBuilder> {
  /// Unique identifier for the story
  @BuiltValueField(wireName: r'id')
  String get id;

  /// The story content as a Quill Delta JSON string (JSON-serialised array of Delta ops).
  @BuiltValueField(wireName: r'story')
  String get story;

  Story._();

  factory Story([void updates(StoryBuilder b)]) = _$Story;

  @BuiltValueHook(initializeBuilder: true)
  static void _defaults(StoryBuilder b) => b;

  @BuiltValueSerializer(custom: true)
  static Serializer<Story> get serializer => _$StorySerializer();
}

class _$StorySerializer implements PrimitiveSerializer<Story> {
  @override
  final Iterable<Type> types = const [Story, _$Story];

  @override
  final String wireName = r'Story';

  Iterable<Object?> _serializeProperties(
    Serializers serializers,
    Story object, {
    FullType specifiedType = FullType.unspecified,
  }) sync* {
    yield r'id';
    yield serializers.serialize(
      object.id,
      specifiedType: const FullType(String),
    );
    yield r'story';
    yield serializers.serialize(
      object.story,
      specifiedType: const FullType(String),
    );
  }

  @override
  Object serialize(
    Serializers serializers,
    Story object, {
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
    required StoryBuilder result,
    required List<Object?> unhandled,
  }) {
    for (var i = 0; i < serializedList.length; i += 2) {
      final key = serializedList[i] as String;
      final value = serializedList[i + 1];
      switch (key) {
        case r'id':
          final valueDes = serializers.deserialize(
            value,
            specifiedType: const FullType(String),
          ) as String;
          result.id = valueDes;
          break;
        case r'story':
          final valueDes = serializers.deserialize(
            value,
            specifiedType: const FullType(String),
          ) as String;
          result.story = valueDes;
          break;
        default:
          unhandled.add(key);
          unhandled.add(value);
          break;
      }
    }
  }

  @override
  Story deserialize(
    Serializers serializers,
    Object serialized, {
    FullType specifiedType = FullType.unspecified,
  }) {
    final result = StoryBuilder();
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
