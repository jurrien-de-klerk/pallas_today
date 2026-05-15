// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'stories_near_you_page.dart';

// **************************************************************************
// BuiltValueGenerator
// **************************************************************************

class _$StoriesNearYouPage extends StoriesNearYouPage {
  @override
  final BuiltList<Story> stories;
  @override
  final DateTime? nextBefore;

  factory _$StoriesNearYouPage(
          [void Function(StoriesNearYouPageBuilder)? updates]) =>
      (StoriesNearYouPageBuilder()..update(updates))._build();

  _$StoriesNearYouPage._({required this.stories, this.nextBefore}) : super._();
  @override
  StoriesNearYouPage rebuild(
          void Function(StoriesNearYouPageBuilder) updates) =>
      (toBuilder()..update(updates)).build();

  @override
  StoriesNearYouPageBuilder toBuilder() =>
      StoriesNearYouPageBuilder()..replace(this);

  @override
  bool operator ==(Object other) {
    if (identical(other, this)) return true;
    return other is StoriesNearYouPage &&
        stories == other.stories &&
        nextBefore == other.nextBefore;
  }

  @override
  int get hashCode {
    var _$hash = 0;
    _$hash = $jc(_$hash, stories.hashCode);
    _$hash = $jc(_$hash, nextBefore.hashCode);
    _$hash = $jf(_$hash);
    return _$hash;
  }

  @override
  String toString() {
    return (newBuiltValueToStringHelper(r'StoriesNearYouPage')
          ..add('stories', stories)
          ..add('nextBefore', nextBefore))
        .toString();
  }
}

class StoriesNearYouPageBuilder
    implements Builder<StoriesNearYouPage, StoriesNearYouPageBuilder> {
  _$StoriesNearYouPage? _$v;

  ListBuilder<Story>? _stories;
  ListBuilder<Story> get stories => _$this._stories ??= ListBuilder<Story>();
  set stories(ListBuilder<Story>? stories) => _$this._stories = stories;

  DateTime? _nextBefore;
  DateTime? get nextBefore => _$this._nextBefore;
  set nextBefore(DateTime? nextBefore) => _$this._nextBefore = nextBefore;

  StoriesNearYouPageBuilder() {
    StoriesNearYouPage._defaults(this);
  }

  StoriesNearYouPageBuilder get _$this {
    final $v = _$v;
    if ($v != null) {
      _stories = $v.stories.toBuilder();
      _nextBefore = $v.nextBefore;
      _$v = null;
    }
    return this;
  }

  @override
  void replace(StoriesNearYouPage other) {
    _$v = other as _$StoriesNearYouPage;
  }

  @override
  void update(void Function(StoriesNearYouPageBuilder)? updates) {
    if (updates != null) updates(this);
  }

  @override
  StoriesNearYouPage build() => _build();

  _$StoriesNearYouPage _build() {
    _$StoriesNearYouPage _$result;
    try {
      _$result = _$v ??
          _$StoriesNearYouPage._(
            stories: stories.build(),
            nextBefore: nextBefore,
          );
    } catch (_) {
      late String _$failedField;
      try {
        _$failedField = 'stories';
        stories.build();
      } catch (e) {
        throw BuiltValueNestedFieldError(
            r'StoriesNearYouPage', _$failedField, e.toString());
      }
      rethrow;
    }
    replace(_$result);
    return _$result;
  }
}

// ignore_for_file: deprecated_member_use_from_same_package,type=lint
