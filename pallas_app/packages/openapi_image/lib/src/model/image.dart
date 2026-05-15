//
// AUTO-GENERATED FILE, DO NOT MODIFY!
//

// ignore_for_file: unused_element
import 'package:built_collection/built_collection.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';

part 'image.g.dart';

/// Image
///
/// Properties:
/// * [id] - Unique identifier for the image
/// * [storyId] - The ID of the story this image belongs to
/// * [storageUrl] - The URL at which the image can be accessed
/// * [contentType] - MIME type of the image
/// * [sizeBytes] - File size in bytes
@BuiltValue()
abstract class Image implements Built<Image, ImageBuilder> {
  /// Unique identifier for the image
  @BuiltValueField(wireName: r'id')
  String get id;

  /// The ID of the story this image belongs to
  @BuiltValueField(wireName: r'storyId')
  String get storyId;

  /// The URL at which the image can be accessed
  @BuiltValueField(wireName: r'storageUrl')
  String get storageUrl;

  /// MIME type of the image
  @BuiltValueField(wireName: r'contentType')
  ImageContentTypeEnum get contentType;
  // enum contentTypeEnum {  image/jpeg,  image/png,  image/webp,  };

  /// File size in bytes
  @BuiltValueField(wireName: r'sizeBytes')
  int get sizeBytes;

  Image._();

  factory Image([void updates(ImageBuilder b)]) = _$Image;

  @BuiltValueHook(initializeBuilder: true)
  static void _defaults(ImageBuilder b) => b;

  @BuiltValueSerializer(custom: true)
  static Serializer<Image> get serializer => _$ImageSerializer();
}

class _$ImageSerializer implements PrimitiveSerializer<Image> {
  @override
  final Iterable<Type> types = const [Image, _$Image];

  @override
  final String wireName = r'Image';

  Iterable<Object?> _serializeProperties(
    Serializers serializers,
    Image object, {
    FullType specifiedType = FullType.unspecified,
  }) sync* {
    yield r'id';
    yield serializers.serialize(
      object.id,
      specifiedType: const FullType(String),
    );
    yield r'storyId';
    yield serializers.serialize(
      object.storyId,
      specifiedType: const FullType(String),
    );
    yield r'storageUrl';
    yield serializers.serialize(
      object.storageUrl,
      specifiedType: const FullType(String),
    );
    yield r'contentType';
    yield serializers.serialize(
      object.contentType,
      specifiedType: const FullType(ImageContentTypeEnum),
    );
    yield r'sizeBytes';
    yield serializers.serialize(
      object.sizeBytes,
      specifiedType: const FullType(int),
    );
  }

  @override
  Object serialize(
    Serializers serializers,
    Image object, {
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
    required ImageBuilder result,
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
        case r'storyId':
          final valueDes = serializers.deserialize(
            value,
            specifiedType: const FullType(String),
          ) as String;
          result.storyId = valueDes;
          break;
        case r'storageUrl':
          final valueDes = serializers.deserialize(
            value,
            specifiedType: const FullType(String),
          ) as String;
          result.storageUrl = valueDes;
          break;
        case r'contentType':
          final valueDes = serializers.deserialize(
            value,
            specifiedType: const FullType(ImageContentTypeEnum),
          ) as ImageContentTypeEnum;
          result.contentType = valueDes;
          break;
        case r'sizeBytes':
          final valueDes = serializers.deserialize(
            value,
            specifiedType: const FullType(int),
          ) as int;
          result.sizeBytes = valueDes;
          break;
        default:
          unhandled.add(key);
          unhandled.add(value);
          break;
      }
    }
  }

  @override
  Image deserialize(
    Serializers serializers,
    Object serialized, {
    FullType specifiedType = FullType.unspecified,
  }) {
    final result = ImageBuilder();
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

class ImageContentTypeEnum extends EnumClass {
  /// MIME type of the image
  @BuiltValueEnumConst(wireName: r'image/jpeg')
  static const ImageContentTypeEnum jpeg = _$imageContentTypeEnum_jpeg;

  /// MIME type of the image
  @BuiltValueEnumConst(wireName: r'image/png')
  static const ImageContentTypeEnum png = _$imageContentTypeEnum_png;

  /// MIME type of the image
  @BuiltValueEnumConst(wireName: r'image/webp')
  static const ImageContentTypeEnum webp = _$imageContentTypeEnum_webp;

  static Serializer<ImageContentTypeEnum> get serializer =>
      _$imageContentTypeEnumSerializer;

  const ImageContentTypeEnum._(String name) : super(name);

  static BuiltSet<ImageContentTypeEnum> get values =>
      _$imageContentTypeEnumValues;
  static ImageContentTypeEnum valueOf(String name) =>
      _$imageContentTypeEnumValueOf(name);
}
