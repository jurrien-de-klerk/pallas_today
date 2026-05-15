// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'story.dart';

// **************************************************************************
// BuiltValueGenerator
// **************************************************************************

class _$Story extends Story {
  @override
  final String id;
  @override
  final String authorId;
  @override
  final String content;
  @override
  final SharedWith sharedWith;
  @override
  final DateTime publishedAt;

  factory _$Story([void Function(StoryBuilder)? updates]) =>
      (StoryBuilder()..update(updates))._build();

  _$Story._(
      {required this.id,
      required this.authorId,
      required this.content,
      required this.sharedWith,
      required this.publishedAt})
      : super._();
  @override
  Story rebuild(void Function(StoryBuilder) updates) =>
      (toBuilder()..update(updates)).build();

  @override
  StoryBuilder toBuilder() => StoryBuilder()..replace(this);

  @override
  bool operator ==(Object other) {
    if (identical(other, this)) return true;
    return other is Story &&
        id == other.id &&
        authorId == other.authorId &&
        content == other.content &&
        sharedWith == other.sharedWith &&
        publishedAt == other.publishedAt;
  }

  @override
  int get hashCode {
    var _$hash = 0;
    _$hash = $jc(_$hash, id.hashCode);
    _$hash = $jc(_$hash, authorId.hashCode);
    _$hash = $jc(_$hash, content.hashCode);
    _$hash = $jc(_$hash, sharedWith.hashCode);
    _$hash = $jc(_$hash, publishedAt.hashCode);
    _$hash = $jf(_$hash);
    return _$hash;
  }

  @override
  String toString() {
    return (newBuiltValueToStringHelper(r'Story')
          ..add('id', id)
          ..add('authorId', authorId)
          ..add('content', content)
          ..add('sharedWith', sharedWith)
          ..add('publishedAt', publishedAt))
        .toString();
  }
}

class StoryBuilder implements Builder<Story, StoryBuilder> {
  _$Story? _$v;

  String? _id;
  String? get id => _$this._id;
  set id(String? id) => _$this._id = id;

  String? _authorId;
  String? get authorId => _$this._authorId;
  set authorId(String? authorId) => _$this._authorId = authorId;

  String? _content;
  String? get content => _$this._content;
  set content(String? content) => _$this._content = content;

  SharedWith? _sharedWith;
  SharedWith? get sharedWith => _$this._sharedWith;
  set sharedWith(SharedWith? sharedWith) => _$this._sharedWith = sharedWith;

  DateTime? _publishedAt;
  DateTime? get publishedAt => _$this._publishedAt;
  set publishedAt(DateTime? publishedAt) => _$this._publishedAt = publishedAt;

  StoryBuilder() {
    Story._defaults(this);
  }

  StoryBuilder get _$this {
    final $v = _$v;
    if ($v != null) {
      _id = $v.id;
      _authorId = $v.authorId;
      _content = $v.content;
      _sharedWith = $v.sharedWith;
      _publishedAt = $v.publishedAt;
      _$v = null;
    }
    return this;
  }

  @override
  void replace(Story other) {
    _$v = other as _$Story;
  }

  @override
  void update(void Function(StoryBuilder)? updates) {
    if (updates != null) updates(this);
  }

  @override
  Story build() => _build();

  _$Story _build() {
    final _$result = _$v ??
        _$Story._(
          id: BuiltValueNullFieldError.checkNotNull(id, r'Story', 'id'),
          authorId: BuiltValueNullFieldError.checkNotNull(
              authorId, r'Story', 'authorId'),
          content: BuiltValueNullFieldError.checkNotNull(
              content, r'Story', 'content'),
          sharedWith: BuiltValueNullFieldError.checkNotNull(
              sharedWith, r'Story', 'sharedWith'),
          publishedAt: BuiltValueNullFieldError.checkNotNull(
              publishedAt, r'Story', 'publishedAt'),
        );
    replace(_$result);
    return _$result;
  }
}

// ignore_for_file: deprecated_member_use_from_same_package,type=lint
