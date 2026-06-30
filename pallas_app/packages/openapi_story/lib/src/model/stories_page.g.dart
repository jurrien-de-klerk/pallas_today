// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'stories_page.dart';

// **************************************************************************
// BuiltValueGenerator
// **************************************************************************

class _$StoriesPage extends StoriesPage {
  @override
  final BuiltList<Story> stories;

  factory _$StoriesPage([void Function(StoriesPageBuilder)? updates]) =>
      (StoriesPageBuilder()..update(updates))._build();

  _$StoriesPage._({required this.stories}) : super._();
  @override
  StoriesPage rebuild(void Function(StoriesPageBuilder) updates) =>
      (toBuilder()..update(updates)).build();

  @override
  StoriesPageBuilder toBuilder() => StoriesPageBuilder()..replace(this);

  @override
  bool operator ==(Object other) {
    if (identical(other, this)) return true;
    return other is StoriesPage && stories == other.stories;
  }

  @override
  int get hashCode {
    var _$hash = 0;
    _$hash = $jc(_$hash, stories.hashCode);
    _$hash = $jf(_$hash);
    return _$hash;
  }

  @override
  String toString() {
    return (newBuiltValueToStringHelper(r'StoriesPage')
          ..add('stories', stories))
        .toString();
  }
}

class StoriesPageBuilder implements Builder<StoriesPage, StoriesPageBuilder> {
  _$StoriesPage? _$v;

  ListBuilder<Story>? _stories;
  ListBuilder<Story> get stories => _$this._stories ??= ListBuilder<Story>();
  set stories(ListBuilder<Story>? stories) => _$this._stories = stories;

  StoriesPageBuilder() {
    StoriesPage._defaults(this);
  }

  StoriesPageBuilder get _$this {
    final $v = _$v;
    if ($v != null) {
      _stories = $v.stories.toBuilder();
      _$v = null;
    }
    return this;
  }

  @override
  void replace(StoriesPage other) {
    _$v = other as _$StoriesPage;
  }

  @override
  void update(void Function(StoriesPageBuilder)? updates) {
    if (updates != null) updates(this);
  }

  @override
  StoriesPage build() => _build();

  _$StoriesPage _build() {
    _$StoriesPage _$result;
    try {
      _$result = _$v ??
          _$StoriesPage._(
            stories: stories.build(),
          );
    } catch (_) {
      late String _$failedField;
      try {
        _$failedField = 'stories';
        stories.build();
      } catch (e) {
        throw BuiltValueNestedFieldError(
            r'StoriesPage', _$failedField, e.toString());
      }
      rethrow;
    }
    replace(_$result);
    return _$result;
  }
}

// ignore_for_file: deprecated_member_use_from_same_package,type=lint
