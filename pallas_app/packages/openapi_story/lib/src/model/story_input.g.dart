// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'story_input.dart';

// **************************************************************************
// BuiltValueGenerator
// **************************************************************************

class _$StoryInput extends StoryInput {
  @override
  final String content;
  @override
  final SharedWith? sharedWith;

  factory _$StoryInput([void Function(StoryInputBuilder)? updates]) =>
      (StoryInputBuilder()..update(updates))._build();

  _$StoryInput._({required this.content, this.sharedWith}) : super._();
  @override
  StoryInput rebuild(void Function(StoryInputBuilder) updates) =>
      (toBuilder()..update(updates)).build();

  @override
  StoryInputBuilder toBuilder() => StoryInputBuilder()..replace(this);

  @override
  bool operator ==(Object other) {
    if (identical(other, this)) return true;
    return other is StoryInput &&
        content == other.content &&
        sharedWith == other.sharedWith;
  }

  @override
  int get hashCode {
    var _$hash = 0;
    _$hash = $jc(_$hash, content.hashCode);
    _$hash = $jc(_$hash, sharedWith.hashCode);
    _$hash = $jf(_$hash);
    return _$hash;
  }

  @override
  String toString() {
    return (newBuiltValueToStringHelper(r'StoryInput')
          ..add('content', content)
          ..add('sharedWith', sharedWith))
        .toString();
  }
}

class StoryInputBuilder implements Builder<StoryInput, StoryInputBuilder> {
  _$StoryInput? _$v;

  String? _content;
  String? get content => _$this._content;
  set content(String? content) => _$this._content = content;

  SharedWith? _sharedWith;
  SharedWith? get sharedWith => _$this._sharedWith;
  set sharedWith(SharedWith? sharedWith) => _$this._sharedWith = sharedWith;

  StoryInputBuilder() {
    StoryInput._defaults(this);
  }

  StoryInputBuilder get _$this {
    final $v = _$v;
    if ($v != null) {
      _content = $v.content;
      _sharedWith = $v.sharedWith;
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
          content: BuiltValueNullFieldError.checkNotNull(
              content, r'StoryInput', 'content'),
          sharedWith: sharedWith,
        );
    replace(_$result);
    return _$result;
  }
}

// ignore_for_file: deprecated_member_use_from_same_package,type=lint
