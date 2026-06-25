// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'quill_delta_operation_insert.dart';

// **************************************************************************
// BuiltValueGenerator
// **************************************************************************

class _$QuillDeltaOperationInsert extends QuillDeltaOperationInsert {
  @override
  final OneOf oneOf;

  factory _$QuillDeltaOperationInsert(
          [void Function(QuillDeltaOperationInsertBuilder)? updates]) =>
      (QuillDeltaOperationInsertBuilder()..update(updates))._build();

  _$QuillDeltaOperationInsert._({required this.oneOf}) : super._();
  @override
  QuillDeltaOperationInsert rebuild(
          void Function(QuillDeltaOperationInsertBuilder) updates) =>
      (toBuilder()..update(updates)).build();

  @override
  QuillDeltaOperationInsertBuilder toBuilder() =>
      QuillDeltaOperationInsertBuilder()..replace(this);

  @override
  bool operator ==(Object other) {
    if (identical(other, this)) return true;
    return other is QuillDeltaOperationInsert && oneOf == other.oneOf;
  }

  @override
  int get hashCode {
    var _$hash = 0;
    _$hash = $jc(_$hash, oneOf.hashCode);
    _$hash = $jf(_$hash);
    return _$hash;
  }

  @override
  String toString() {
    return (newBuiltValueToStringHelper(r'QuillDeltaOperationInsert')
          ..add('oneOf', oneOf))
        .toString();
  }
}

class QuillDeltaOperationInsertBuilder
    implements
        Builder<QuillDeltaOperationInsert, QuillDeltaOperationInsertBuilder> {
  _$QuillDeltaOperationInsert? _$v;

  OneOf? _oneOf;
  OneOf? get oneOf => _$this._oneOf;
  set oneOf(OneOf? oneOf) => _$this._oneOf = oneOf;

  QuillDeltaOperationInsertBuilder() {
    QuillDeltaOperationInsert._defaults(this);
  }

  QuillDeltaOperationInsertBuilder get _$this {
    final $v = _$v;
    if ($v != null) {
      _oneOf = $v.oneOf;
      _$v = null;
    }
    return this;
  }

  @override
  void replace(QuillDeltaOperationInsert other) {
    _$v = other as _$QuillDeltaOperationInsert;
  }

  @override
  void update(void Function(QuillDeltaOperationInsertBuilder)? updates) {
    if (updates != null) updates(this);
  }

  @override
  QuillDeltaOperationInsert build() => _build();

  _$QuillDeltaOperationInsert _build() {
    final _$result = _$v ??
        _$QuillDeltaOperationInsert._(
          oneOf: BuiltValueNullFieldError.checkNotNull(
              oneOf, r'QuillDeltaOperationInsert', 'oneOf'),
        );
    replace(_$result);
    return _$result;
  }
}

// ignore_for_file: deprecated_member_use_from_same_package,type=lint
