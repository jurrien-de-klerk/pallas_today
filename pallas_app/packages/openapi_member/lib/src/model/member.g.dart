// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'member.dart';

// **************************************************************************
// BuiltValueGenerator
// **************************************************************************

class _$Member extends Member {
  @override
  final String memberId;
  @override
  final String firstName;
  @override
  final String lastName;

  factory _$Member([void Function(MemberBuilder)? updates]) =>
      (MemberBuilder()..update(updates))._build();

  _$Member._(
      {required this.memberId, required this.firstName, required this.lastName})
      : super._();
  @override
  Member rebuild(void Function(MemberBuilder) updates) =>
      (toBuilder()..update(updates)).build();

  @override
  MemberBuilder toBuilder() => MemberBuilder()..replace(this);

  @override
  bool operator ==(Object other) {
    if (identical(other, this)) return true;
    return other is Member &&
        memberId == other.memberId &&
        firstName == other.firstName &&
        lastName == other.lastName;
  }

  @override
  int get hashCode {
    var _$hash = 0;
    _$hash = $jc(_$hash, memberId.hashCode);
    _$hash = $jc(_$hash, firstName.hashCode);
    _$hash = $jc(_$hash, lastName.hashCode);
    _$hash = $jf(_$hash);
    return _$hash;
  }

  @override
  String toString() {
    return (newBuiltValueToStringHelper(r'Member')
          ..add('memberId', memberId)
          ..add('firstName', firstName)
          ..add('lastName', lastName))
        .toString();
  }
}

class MemberBuilder implements Builder<Member, MemberBuilder> {
  _$Member? _$v;

  String? _memberId;
  String? get memberId => _$this._memberId;
  set memberId(String? memberId) => _$this._memberId = memberId;

  String? _firstName;
  String? get firstName => _$this._firstName;
  set firstName(String? firstName) => _$this._firstName = firstName;

  String? _lastName;
  String? get lastName => _$this._lastName;
  set lastName(String? lastName) => _$this._lastName = lastName;

  MemberBuilder() {
    Member._defaults(this);
  }

  MemberBuilder get _$this {
    final $v = _$v;
    if ($v != null) {
      _memberId = $v.memberId;
      _firstName = $v.firstName;
      _lastName = $v.lastName;
      _$v = null;
    }
    return this;
  }

  @override
  void replace(Member other) {
    _$v = other as _$Member;
  }

  @override
  void update(void Function(MemberBuilder)? updates) {
    if (updates != null) updates(this);
  }

  @override
  Member build() => _build();

  _$Member _build() {
    final _$result = _$v ??
        _$Member._(
          memberId: BuiltValueNullFieldError.checkNotNull(
              memberId, r'Member', 'memberId'),
          firstName: BuiltValueNullFieldError.checkNotNull(
              firstName, r'Member', 'firstName'),
          lastName: BuiltValueNullFieldError.checkNotNull(
              lastName, r'Member', 'lastName'),
        );
    replace(_$result);
    return _$result;
  }
}

// ignore_for_file: deprecated_member_use_from_same_package,type=lint
