// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'quill_delta_operation.dart';

// **************************************************************************
// BuiltValueGenerator
// **************************************************************************

class _$QuillDeltaOperation extends QuillDeltaOperation {
  @override
  final QuillDeltaOperationInsert insert;
  @override
  final BuiltMap<String, JsonObject?>? attributes;

  factory _$QuillDeltaOperation(
          [void Function(QuillDeltaOperationBuilder)? updates]) =>
      (QuillDeltaOperationBuilder()..update(updates))._build();

  _$QuillDeltaOperation._({required this.insert, this.attributes}) : super._();
  @override
  QuillDeltaOperation rebuild(
          void Function(QuillDeltaOperationBuilder) updates) =>
      (toBuilder()..update(updates)).build();

  @override
  QuillDeltaOperationBuilder toBuilder() =>
      QuillDeltaOperationBuilder()..replace(this);

  @override
  bool operator ==(Object other) {
    if (identical(other, this)) return true;
    return other is QuillDeltaOperation &&
        insert == other.insert &&
        attributes == other.attributes;
  }

  @override
  int get hashCode {
    var _$hash = 0;
    _$hash = $jc(_$hash, insert.hashCode);
    _$hash = $jc(_$hash, attributes.hashCode);
    _$hash = $jf(_$hash);
    return _$hash;
  }

  @override
  String toString() {
    return (newBuiltValueToStringHelper(r'QuillDeltaOperation')
          ..add('insert', insert)
          ..add('attributes', attributes))
        .toString();
  }
}

class QuillDeltaOperationBuilder
    implements Builder<QuillDeltaOperation, QuillDeltaOperationBuilder> {
  _$QuillDeltaOperation? _$v;

  QuillDeltaOperationInsertBuilder? _insert;
  QuillDeltaOperationInsertBuilder get insert =>
      _$this._insert ??= QuillDeltaOperationInsertBuilder();
  set insert(QuillDeltaOperationInsertBuilder? insert) =>
      _$this._insert = insert;

  MapBuilder<String, JsonObject?>? _attributes;
  MapBuilder<String, JsonObject?> get attributes =>
      _$this._attributes ??= MapBuilder<String, JsonObject?>();
  set attributes(MapBuilder<String, JsonObject?>? attributes) =>
      _$this._attributes = attributes;

  QuillDeltaOperationBuilder() {
    QuillDeltaOperation._defaults(this);
  }

  QuillDeltaOperationBuilder get _$this {
    final $v = _$v;
    if ($v != null) {
      _insert = $v.insert.toBuilder();
      _attributes = $v.attributes?.toBuilder();
      _$v = null;
    }
    return this;
  }

  @override
  void replace(QuillDeltaOperation other) {
    _$v = other as _$QuillDeltaOperation;
  }

  @override
  void update(void Function(QuillDeltaOperationBuilder)? updates) {
    if (updates != null) updates(this);
  }

  @override
  QuillDeltaOperation build() => _build();

  _$QuillDeltaOperation _build() {
    _$QuillDeltaOperation _$result;
    try {
      _$result = _$v ??
          _$QuillDeltaOperation._(
            insert: insert.build(),
            attributes: _attributes?.build(),
          );
    } catch (_) {
      late String _$failedField;
      try {
        _$failedField = 'insert';
        insert.build();
        _$failedField = 'attributes';
        _attributes?.build();
      } catch (e) {
        throw BuiltValueNestedFieldError(
            r'QuillDeltaOperation', _$failedField, e.toString());
      }
      rethrow;
    }
    replace(_$result);
    return _$result;
  }
}

// ignore_for_file: deprecated_member_use_from_same_package,type=lint
