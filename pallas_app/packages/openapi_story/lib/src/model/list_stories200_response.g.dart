// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'list_stories200_response.dart';

// **************************************************************************
// BuiltValueGenerator
// **************************************************************************

class _$ListStories200Response extends ListStories200Response {
  @override
  final BuiltList<Story>? stories;
  @override
  final int? total;
  @override
  final int? limit;
  @override
  final int? offset;

  factory _$ListStories200Response(
          [void Function(ListStories200ResponseBuilder)? updates]) =>
      (ListStories200ResponseBuilder()..update(updates))._build();

  _$ListStories200Response._(
      {this.stories, this.total, this.limit, this.offset})
      : super._();
  @override
  ListStories200Response rebuild(
          void Function(ListStories200ResponseBuilder) updates) =>
      (toBuilder()..update(updates)).build();

  @override
  ListStories200ResponseBuilder toBuilder() =>
      ListStories200ResponseBuilder()..replace(this);

  @override
  bool operator ==(Object other) {
    if (identical(other, this)) return true;
    return other is ListStories200Response &&
        stories == other.stories &&
        total == other.total &&
        limit == other.limit &&
        offset == other.offset;
  }

  @override
  int get hashCode {
    var _$hash = 0;
    _$hash = $jc(_$hash, stories.hashCode);
    _$hash = $jc(_$hash, total.hashCode);
    _$hash = $jc(_$hash, limit.hashCode);
    _$hash = $jc(_$hash, offset.hashCode);
    _$hash = $jf(_$hash);
    return _$hash;
  }

  @override
  String toString() {
    return (newBuiltValueToStringHelper(r'ListStories200Response')
          ..add('stories', stories)
          ..add('total', total)
          ..add('limit', limit)
          ..add('offset', offset))
        .toString();
  }
}

class ListStories200ResponseBuilder
    implements Builder<ListStories200Response, ListStories200ResponseBuilder> {
  _$ListStories200Response? _$v;

  ListBuilder<Story>? _stories;
  ListBuilder<Story> get stories => _$this._stories ??= ListBuilder<Story>();
  set stories(ListBuilder<Story>? stories) => _$this._stories = stories;

  int? _total;
  int? get total => _$this._total;
  set total(int? total) => _$this._total = total;

  int? _limit;
  int? get limit => _$this._limit;
  set limit(int? limit) => _$this._limit = limit;

  int? _offset;
  int? get offset => _$this._offset;
  set offset(int? offset) => _$this._offset = offset;

  ListStories200ResponseBuilder() {
    ListStories200Response._defaults(this);
  }

  ListStories200ResponseBuilder get _$this {
    final $v = _$v;
    if ($v != null) {
      _stories = $v.stories?.toBuilder();
      _total = $v.total;
      _limit = $v.limit;
      _offset = $v.offset;
      _$v = null;
    }
    return this;
  }

  @override
  void replace(ListStories200Response other) {
    _$v = other as _$ListStories200Response;
  }

  @override
  void update(void Function(ListStories200ResponseBuilder)? updates) {
    if (updates != null) updates(this);
  }

  @override
  ListStories200Response build() => _build();

  _$ListStories200Response _build() {
    _$ListStories200Response _$result;
    try {
      _$result = _$v ??
          _$ListStories200Response._(
            stories: _stories?.build(),
            total: total,
            limit: limit,
            offset: offset,
          );
    } catch (_) {
      late String _$failedField;
      try {
        _$failedField = 'stories';
        _stories?.build();
      } catch (e) {
        throw BuiltValueNestedFieldError(
            r'ListStories200Response', _$failedField, e.toString());
      }
      rethrow;
    }
    replace(_$result);
    return _$result;
  }
}

// ignore_for_file: deprecated_member_use_from_same_package,type=lint
