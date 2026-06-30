//
// AUTO-GENERATED FILE, DO NOT MODIFY!
//

// ignore_for_file: unused_element
import 'package:openapi_story/src/model/story.dart';
import 'package:built_collection/built_collection.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';

part 'stories_page.g.dart';

/// StoriesPage
///
/// Properties:
/// * [stories] - Stories ordered by publishedAt descending. Only stories visible to the requesting member are included.
@BuiltValue()
abstract class StoriesPage implements Built<StoriesPage, StoriesPageBuilder> {
  /// Stories ordered by publishedAt descending. Only stories visible to the requesting member are included.
  @BuiltValueField(wireName: r'stories')
  BuiltList<Story> get stories;

  StoriesPage._();

  factory StoriesPage([void updates(StoriesPageBuilder b)]) = _$StoriesPage;

  @BuiltValueHook(initializeBuilder: true)
  static void _defaults(StoriesPageBuilder b) => b;

  @BuiltValueSerializer(custom: true)
  static Serializer<StoriesPage> get serializer => _$StoriesPageSerializer();
}

class _$StoriesPageSerializer implements PrimitiveSerializer<StoriesPage> {
  @override
  final Iterable<Type> types = const [StoriesPage, _$StoriesPage];

  @override
  final String wireName = r'StoriesPage';

  Iterable<Object?> _serializeProperties(
    Serializers serializers,
    StoriesPage object, {
    FullType specifiedType = FullType.unspecified,
  }) sync* {
    yield r'stories';
    yield serializers.serialize(
      object.stories,
      specifiedType: const FullType(BuiltList, [FullType(Story)]),
    );
  }

  @override
  Object serialize(
    Serializers serializers,
    StoriesPage object, {
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
    required StoriesPageBuilder result,
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
        default:
          unhandled.add(key);
          unhandled.add(value);
          break;
      }
    }
  }

  @override
  StoriesPage deserialize(
    Serializers serializers,
    Object serialized, {
    FullType specifiedType = FullType.unspecified,
  }) {
    final result = StoriesPageBuilder();
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
