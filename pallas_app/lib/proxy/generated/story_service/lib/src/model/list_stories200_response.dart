//
// AUTO-GENERATED FILE, DO NOT MODIFY!
//

// ignore_for_file: unused_element
import 'package:built_collection/built_collection.dart';
import 'package:openapi/src/model/story.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';

part 'list_stories200_response.g.dart';

/// ListStories200Response
///
/// Properties:
/// * [stories]
/// * [total] - Total number of stories
/// * [limit]
/// * [offset]
@BuiltValue()
abstract class ListStories200Response
    implements Built<ListStories200Response, ListStories200ResponseBuilder> {
  @BuiltValueField(wireName: r'stories')
  BuiltList<Story>? get stories;

  /// Total number of stories
  @BuiltValueField(wireName: r'total')
  int? get total;

  @BuiltValueField(wireName: r'limit')
  int? get limit;

  @BuiltValueField(wireName: r'offset')
  int? get offset;

  ListStories200Response._();

  factory ListStories200Response(
          [void updates(ListStories200ResponseBuilder b)]) =
      _$ListStories200Response;

  @BuiltValueHook(initializeBuilder: true)
  static void _defaults(ListStories200ResponseBuilder b) => b;

  @BuiltValueSerializer(custom: true)
  static Serializer<ListStories200Response> get serializer =>
      _$ListStories200ResponseSerializer();
}

class _$ListStories200ResponseSerializer
    implements PrimitiveSerializer<ListStories200Response> {
  @override
  final Iterable<Type> types = const [
    ListStories200Response,
    _$ListStories200Response
  ];

  @override
  final String wireName = r'ListStories200Response';

  Iterable<Object?> _serializeProperties(
    Serializers serializers,
    ListStories200Response object, {
    FullType specifiedType = FullType.unspecified,
  }) sync* {
    if (object.stories != null) {
      yield r'stories';
      yield serializers.serialize(
        object.stories,
        specifiedType: const FullType(BuiltList, [FullType(Story)]),
      );
    }
    if (object.total != null) {
      yield r'total';
      yield serializers.serialize(
        object.total,
        specifiedType: const FullType(int),
      );
    }
    if (object.limit != null) {
      yield r'limit';
      yield serializers.serialize(
        object.limit,
        specifiedType: const FullType(int),
      );
    }
    if (object.offset != null) {
      yield r'offset';
      yield serializers.serialize(
        object.offset,
        specifiedType: const FullType(int),
      );
    }
  }

  @override
  Object serialize(
    Serializers serializers,
    ListStories200Response object, {
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
    required ListStories200ResponseBuilder result,
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
        case r'total':
          final valueDes = serializers.deserialize(
            value,
            specifiedType: const FullType(int),
          ) as int;
          result.total = valueDes;
          break;
        case r'limit':
          final valueDes = serializers.deserialize(
            value,
            specifiedType: const FullType(int),
          ) as int;
          result.limit = valueDes;
          break;
        case r'offset':
          final valueDes = serializers.deserialize(
            value,
            specifiedType: const FullType(int),
          ) as int;
          result.offset = valueDes;
          break;
        default:
          unhandled.add(key);
          unhandled.add(value);
          break;
      }
    }
  }

  @override
  ListStories200Response deserialize(
    Serializers serializers,
    Object serialized, {
    FullType specifiedType = FullType.unspecified,
  }) {
    final result = ListStories200ResponseBuilder();
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
