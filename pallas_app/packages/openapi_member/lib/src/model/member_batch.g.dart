// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'member_batch.dart';

// **************************************************************************
// BuiltValueGenerator
// **************************************************************************

class _$MemberBatch extends MemberBatch {
  @override
  final BuiltList<Member> members;

  factory _$MemberBatch([void Function(MemberBatchBuilder)? updates]) =>
      (MemberBatchBuilder()..update(updates))._build();

  _$MemberBatch._({required this.members}) : super._();
  @override
  MemberBatch rebuild(void Function(MemberBatchBuilder) updates) =>
      (toBuilder()..update(updates)).build();

  @override
  MemberBatchBuilder toBuilder() => MemberBatchBuilder()..replace(this);

  @override
  bool operator ==(Object other) {
    if (identical(other, this)) return true;
    return other is MemberBatch && members == other.members;
  }

  @override
  int get hashCode {
    var _$hash = 0;
    _$hash = $jc(_$hash, members.hashCode);
    _$hash = $jf(_$hash);
    return _$hash;
  }

  @override
  String toString() {
    return (newBuiltValueToStringHelper(r'MemberBatch')
          ..add('members', members))
        .toString();
  }
}

class MemberBatchBuilder implements Builder<MemberBatch, MemberBatchBuilder> {
  _$MemberBatch? _$v;

  ListBuilder<Member>? _members;
  ListBuilder<Member> get members => _$this._members ??= ListBuilder<Member>();
  set members(ListBuilder<Member>? members) => _$this._members = members;

  MemberBatchBuilder() {
    MemberBatch._defaults(this);
  }

  MemberBatchBuilder get _$this {
    final $v = _$v;
    if ($v != null) {
      _members = $v.members.toBuilder();
      _$v = null;
    }
    return this;
  }

  @override
  void replace(MemberBatch other) {
    _$v = other as _$MemberBatch;
  }

  @override
  void update(void Function(MemberBatchBuilder)? updates) {
    if (updates != null) updates(this);
  }

  @override
  MemberBatch build() => _build();

  _$MemberBatch _build() {
    _$MemberBatch _$result;
    try {
      _$result = _$v ??
          _$MemberBatch._(
            members: members.build(),
          );
    } catch (_) {
      late String _$failedField;
      try {
        _$failedField = 'members';
        members.build();
      } catch (e) {
        throw BuiltValueNestedFieldError(
            r'MemberBatch', _$failedField, e.toString());
      }
      rethrow;
    }
    replace(_$result);
    return _$result;
  }
}

// ignore_for_file: deprecated_member_use_from_same_package,type=lint
