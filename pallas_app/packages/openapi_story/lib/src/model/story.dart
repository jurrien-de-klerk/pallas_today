//
// AUTO-GENERATED FILE, DO NOT MODIFY!
//

// ignore_for_file: unused_element
import 'package:openapi_story/src/model/shared_with.dart';
import 'package:built_collection/built_collection.dart';
import 'package:openapi_story/src/model/quill_delta_operation.dart';
import 'package:built_value/json_object.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';

part 'story.g.dart';

/// Story
///
/// Properties:
/// * [id] - Unique identifier of the story
/// * [authorId] - The memberId of the member who published the story
/// * [content]
/// * [sharedWith]
/// * [publishedAt] - The date and time at which the story was published (ISO 8601, UTC). Used to order stories in the Stories near you feed.
@BuiltValue()
abstract class Story implements Built<Story, StoryBuilder> {
  /// Unique identifier of the story
  @BuiltValueField(wireName: r'id')
  String get id;

  /// The memberId of the member who published the story
  @BuiltValueField(wireName: r'authorId')
  String get authorId;

  @BuiltValueField(wireName: r'content')
  BuiltList get content;

  @BuiltValueField(wireName: r'sharedWith')
  SharedWith get sharedWith;
  // enum sharedWithEnum {  TRUSTED,  CONNECTED,  COMMUNITY,  PUBLIC,  };

  /// The date and time at which the story was published (ISO 8601, UTC). Used to order stories in the Stories near you feed.
  @BuiltValueField(wireName: r'publishedAt')
  DateTime get publishedAt;

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
    yield r'authorId';
    yield serializers.serialize(
      object.authorId,
      specifiedType: const FullType(String),
    );
    yield r'content';
    yield serializers.serialize(
      object.content,
      specifiedType: const FullType(BuiltList, [FullType(JsonObject)]),
    );
    yield r'sharedWith';
    yield serializers.serialize(
      object.sharedWith,
      specifiedType: const FullType(SharedWith),
    );
    yield r'publishedAt';
    yield serializers.serialize(
      object.publishedAt,
      specifiedType: const FullType(DateTime),
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
        case r'authorId':
          final valueDes = serializers.deserialize(
            value,
            specifiedType: const FullType(String),
          ) as String;
          result.authorId = valueDes;
          break;
        case r'content':
          final valueDes = serializers.deserialize(
            value,
            specifiedType: const FullType(BuiltList, [FullType(JsonObject)]),
          ) as BuiltList;
          result.content.replace(valueDes);
          break;
        case r'sharedWith':
          final valueDes = serializers.deserialize(
            value,
            specifiedType: const FullType(SharedWith),
          ) as SharedWith;
          result.sharedWith = valueDes;
          break;
        case r'publishedAt':
          final valueDes = serializers.deserialize(
            value,
            specifiedType: const FullType(DateTime),
          ) as DateTime;
          result.publishedAt = valueDes;
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
