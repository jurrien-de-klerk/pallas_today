//
// AUTO-GENERATED FILE, DO NOT MODIFY!
//

// ignore_for_file: unused_element
import 'package:openapi_story/src/model/story.dart';
import 'package:built_collection/built_collection.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';

part 'stories_near_you_page.g.dart';

/// StoriesNearYouPage
///
/// Properties:
/// * [stories] - Stories ordered by publishedAt descending. Only stories visible to the requesting member are included.
/// * [nextBefore] - Pass this value as the `before` query parameter to fetch the next page. Absent when there are no more stories to return.
@BuiltValue()
abstract class StoriesNearYouPage
    implements Built<StoriesNearYouPage, StoriesNearYouPageBuilder> {
  /// Stories ordered by publishedAt descending. Only stories visible to the requesting member are included.
  @BuiltValueField(wireName: r'stories')
  BuiltList<Story> get stories;

  /// Pass this value as the `before` query parameter to fetch the next page. Absent when there are no more stories to return.
  @BuiltValueField(wireName: r'nextBefore')
  DateTime? get nextBefore;

  StoriesNearYouPage._();

  factory StoriesNearYouPage([void updates(StoriesNearYouPageBuilder b)]) =
      _$StoriesNearYouPage;

  @BuiltValueHook(initializeBuilder: true)
  static void _defaults(StoriesNearYouPageBuilder b) => b;

  @BuiltValueSerializer(custom: true)
  static Serializer<StoriesNearYouPage> get serializer =>
      _$StoriesNearYouPageSerializer();
}

class _$StoriesNearYouPageSerializer
    implements PrimitiveSerializer<StoriesNearYouPage> {
  @override
  final Iterable<Type> types = const [StoriesNearYouPage, _$StoriesNearYouPage];

  @override
  final String wireName = r'StoriesNearYouPage';

  Iterable<Object?> _serializeProperties(
    Serializers serializers,
    StoriesNearYouPage object, {
    FullType specifiedType = FullType.unspecified,
  }) sync* {
    yield r'stories';
    yield serializers.serialize(
      object.stories,
      specifiedType: const FullType(BuiltList, [FullType(Story)]),
    );
    if (object.nextBefore != null) {
      yield r'nextBefore';
      yield serializers.serialize(
        object.nextBefore,
        specifiedType: const FullType(DateTime),
      );
    }
  }

  @override
  Object serialize(
    Serializers serializers,
    StoriesNearYouPage object, {
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
    required StoriesNearYouPageBuilder result,
    required List<Object?> unhandled,
  }) {
    for (var i = 0; i < serializedList.length; i += 2) {
      final key = serializedList[i] as String;
      final value = serializedList[i + 1];
      switch (key) {
        case r'stories':
          final valueDes = serializers.deserialize(
            value,
            specifiedType: const FullType(BuiltList, [FullType(Story)]),
          ) as BuiltList<Story>;
          result.stories.replace(valueDes);
          break;
        case r'nextBefore':
          final valueDes = serializers.deserialize(
            value,
            specifiedType: const FullType(DateTime),
          ) as DateTime;
          result.nextBefore = valueDes;
          break;
        default:
          unhandled.add(key);
          unhandled.add(value);
          break;
      }
    }
  }

  @override
  StoriesNearYouPage deserialize(
    Serializers serializers,
    Object serialized, {
    FullType specifiedType = FullType.unspecified,
  }) {
    final result = StoriesNearYouPageBuilder();
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
