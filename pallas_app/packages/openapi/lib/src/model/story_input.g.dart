// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'story_input.dart';

// **************************************************************************
// BuiltValueGenerator
// **************************************************************************

class _$StoryInput extends StoryInput {
  @override
  final String story;

  factory _$StoryInput([void Function(StoryInputBuilder)? updates]) =>
      (StoryInputBuilder()..update(updates))._build();

  _$StoryInput._({required this.story}) : super._();
  @override
  StoryInput rebuild(void Function(StoryInputBuilder) updates) =>
      (toBuilder()..update(updates)).build();

  @override
  StoryInputBuilder toBuilder() => StoryInputBuilder()..replace(this);

  @override
  bool operator ==(Object other) {
    if (identical(other, this)) return true;
    return other is StoryInput && story == other.story;
  }

  @override
  int get hashCode {
    var _$hash = 0;
    _$hash = $jc(_$hash, story.hashCode);
    _$hash = $jf(_$hash);
    return _$hash;
  }

  @override
  String toString() {
    return (newBuiltValueToStringHelper(r'StoryInput')..add('story', story))
        .toString();
  }
}

class StoryInputBuilder implements Builder<StoryInput, StoryInputBuilder> {
  _$StoryInput? _$v;

  String? _story;
  String? get story => _$this._story;
  set story(String? story) => _$this._story = story;

  StoryInputBuilder() {
    StoryInput._defaults(this);
  }

  StoryInputBuilder get _$this {
    final $v = _$v;
    if ($v != null) {
      _story = $v.story;
      _$v = null;
    }
    return this;
  }

  @override
  void replace(StoryInput other) {
    _$v = other as _$StoryInput;
  }

  @override
  void update(void Function(StoryInputBuilder)? updates) {
    if (updates != null) updates(this);
  }

  @override
  StoryInput build() => _build();

  _$StoryInput _build() {
    final _$result = _$v ??
        _$StoryInput._(
          story: BuiltValueNullFieldError.checkNotNull(
              story, r'StoryInput', 'story'),
        );
    replace(_$result);
    return _$result;
  }
}

// ignore_for_file: deprecated_member_use_from_same_package,type=lint
