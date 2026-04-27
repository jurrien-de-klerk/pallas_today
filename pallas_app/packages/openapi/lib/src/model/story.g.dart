// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'story.dart';

// **************************************************************************
// BuiltValueGenerator
// **************************************************************************

class _$Story extends Story {
  @override
  final String id;
  @override
  final String story;

  factory _$Story([void Function(StoryBuilder)? updates]) =>
      (StoryBuilder()..update(updates))._build();

  _$Story._({required this.id, required this.story}) : super._();
  @override
  Story rebuild(void Function(StoryBuilder) updates) =>
      (toBuilder()..update(updates)).build();

  @override
  StoryBuilder toBuilder() => StoryBuilder()..replace(this);

  @override
  bool operator ==(Object other) {
    if (identical(other, this)) return true;
    return other is Story && id == other.id && story == other.story;
  }

  @override
  int get hashCode {
    var _$hash = 0;
    _$hash = $jc(_$hash, id.hashCode);
    _$hash = $jc(_$hash, story.hashCode);
    _$hash = $jf(_$hash);
    return _$hash;
  }

  @override
  String toString() {
    return (newBuiltValueToStringHelper(r'Story')
          ..add('id', id)
          ..add('story', story))
        .toString();
  }
}

class StoryBuilder implements Builder<Story, StoryBuilder> {
  _$Story? _$v;

  String? _id;
  String? get id => _$this._id;
  set id(String? id) => _$this._id = id;

  String? _story;
  String? get story => _$this._story;
  set story(String? story) => _$this._story = story;

  StoryBuilder() {
    Story._defaults(this);
  }

  StoryBuilder get _$this {
    final $v = _$v;
    if ($v != null) {
      _id = $v.id;
      _story = $v.story;
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
          story:
              BuiltValueNullFieldError.checkNotNull(story, r'Story', 'story'),
        );
    replace(_$result);
    return _$result;
  }
}

// ignore_for_file: deprecated_member_use_from_same_package,type=lint
