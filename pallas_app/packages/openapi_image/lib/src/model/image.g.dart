// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'image.dart';

// **************************************************************************
// BuiltValueGenerator
// **************************************************************************

const ImageContentTypeEnum _$imageContentTypeEnum_jpeg =
    const ImageContentTypeEnum._('jpeg');
const ImageContentTypeEnum _$imageContentTypeEnum_png =
    const ImageContentTypeEnum._('png');
const ImageContentTypeEnum _$imageContentTypeEnum_webp =
    const ImageContentTypeEnum._('webp');

ImageContentTypeEnum _$imageContentTypeEnumValueOf(String name) {
  switch (name) {
    case 'jpeg':
      return _$imageContentTypeEnum_jpeg;
    case 'png':
      return _$imageContentTypeEnum_png;
    case 'webp':
      return _$imageContentTypeEnum_webp;
    default:
      throw ArgumentError(name);
  }
}

final BuiltSet<ImageContentTypeEnum> _$imageContentTypeEnumValues =
    BuiltSet<ImageContentTypeEnum>(const <ImageContentTypeEnum>[
  _$imageContentTypeEnum_jpeg,
  _$imageContentTypeEnum_png,
  _$imageContentTypeEnum_webp,
]);

Serializer<ImageContentTypeEnum> _$imageContentTypeEnumSerializer =
    _$ImageContentTypeEnumSerializer();

class _$ImageContentTypeEnumSerializer
    implements PrimitiveSerializer<ImageContentTypeEnum> {
  static const Map<String, Object> _toWire = const <String, Object>{
    'jpeg': 'image/jpeg',
    'png': 'image/png',
    'webp': 'image/webp',
  };
  static const Map<Object, String> _fromWire = const <Object, String>{
    'image/jpeg': 'jpeg',
    'image/png': 'png',
    'image/webp': 'webp',
  };

  @override
  final Iterable<Type> types = const <Type>[ImageContentTypeEnum];
  @override
  final String wireName = 'ImageContentTypeEnum';

  @override
  Object serialize(Serializers serializers, ImageContentTypeEnum object,
          {FullType specifiedType = FullType.unspecified}) =>
      _toWire[object.name] ?? object.name;

  @override
  ImageContentTypeEnum deserialize(Serializers serializers, Object serialized,
          {FullType specifiedType = FullType.unspecified}) =>
      ImageContentTypeEnum.valueOf(
          _fromWire[serialized] ?? (serialized is String ? serialized : ''));
}

class _$Image extends Image {
  @override
  final String id;
  @override
  final String storyId;
  @override
  final String storageUrl;
  @override
  final ImageContentTypeEnum contentType;
  @override
  final int sizeBytes;

  factory _$Image([void Function(ImageBuilder)? updates]) =>
      (ImageBuilder()..update(updates))._build();

  _$Image._(
      {required this.id,
      required this.storyId,
      required this.storageUrl,
      required this.contentType,
      required this.sizeBytes})
      : super._();
  @override
  Image rebuild(void Function(ImageBuilder) updates) =>
      (toBuilder()..update(updates)).build();

  @override
  ImageBuilder toBuilder() => ImageBuilder()..replace(this);

  @override
  bool operator ==(Object other) {
    if (identical(other, this)) return true;
    return other is Image &&
        id == other.id &&
        storyId == other.storyId &&
        storageUrl == other.storageUrl &&
        contentType == other.contentType &&
        sizeBytes == other.sizeBytes;
  }

  @override
  int get hashCode {
    var _$hash = 0;
    _$hash = $jc(_$hash, id.hashCode);
    _$hash = $jc(_$hash, storyId.hashCode);
    _$hash = $jc(_$hash, storageUrl.hashCode);
    _$hash = $jc(_$hash, contentType.hashCode);
    _$hash = $jc(_$hash, sizeBytes.hashCode);
    _$hash = $jf(_$hash);
    return _$hash;
  }

  @override
  String toString() {
    return (newBuiltValueToStringHelper(r'Image')
          ..add('id', id)
          ..add('storyId', storyId)
          ..add('storageUrl', storageUrl)
          ..add('contentType', contentType)
          ..add('sizeBytes', sizeBytes))
        .toString();
  }
}

class ImageBuilder implements Builder<Image, ImageBuilder> {
  _$Image? _$v;

  String? _id;
  String? get id => _$this._id;
  set id(String? id) => _$this._id = id;

  String? _storyId;
  String? get storyId => _$this._storyId;
  set storyId(String? storyId) => _$this._storyId = storyId;

  String? _storageUrl;
  String? get storageUrl => _$this._storageUrl;
  set storageUrl(String? storageUrl) => _$this._storageUrl = storageUrl;

  ImageContentTypeEnum? _contentType;
  ImageContentTypeEnum? get contentType => _$this._contentType;
  set contentType(ImageContentTypeEnum? contentType) =>
      _$this._contentType = contentType;

  int? _sizeBytes;
  int? get sizeBytes => _$this._sizeBytes;
  set sizeBytes(int? sizeBytes) => _$this._sizeBytes = sizeBytes;

  ImageBuilder() {
    Image._defaults(this);
  }

  ImageBuilder get _$this {
    final $v = _$v;
    if ($v != null) {
      _id = $v.id;
      _storyId = $v.storyId;
      _storageUrl = $v.storageUrl;
      _contentType = $v.contentType;
      _sizeBytes = $v.sizeBytes;
      _$v = null;
    }
    return this;
  }

  @override
  void replace(Image other) {
    _$v = other as _$Image;
  }

  @override
  void update(void Function(ImageBuilder)? updates) {
    if (updates != null) updates(this);
  }

  @override
  Image build() => _build();

  _$Image _build() {
    final _$result = _$v ??
        _$Image._(
          id: BuiltValueNullFieldError.checkNotNull(id, r'Image', 'id'),
          storyId: BuiltValueNullFieldError.checkNotNull(
              storyId, r'Image', 'storyId'),
          storageUrl: BuiltValueNullFieldError.checkNotNull(
              storageUrl, r'Image', 'storageUrl'),
          contentType: BuiltValueNullFieldError.checkNotNull(
              contentType, r'Image', 'contentType'),
          sizeBytes: BuiltValueNullFieldError.checkNotNull(
              sizeBytes, r'Image', 'sizeBytes'),
        );
    replace(_$result);
    return _$result;
  }
}

// ignore_for_file: deprecated_member_use_from_same_package,type=lint
