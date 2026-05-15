// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'shared_with.dart';

// **************************************************************************
// BuiltValueGenerator
// **************************************************************************

const SharedWith _$TRUSTED = const SharedWith._('TRUSTED');
const SharedWith _$CONNECTED = const SharedWith._('CONNECTED');
const SharedWith _$COMMUNITY = const SharedWith._('COMMUNITY');
const SharedWith _$PUBLIC = const SharedWith._('PUBLIC');

SharedWith _$valueOf(String name) {
  switch (name) {
    case 'TRUSTED':
      return _$TRUSTED;
    case 'CONNECTED':
      return _$CONNECTED;
    case 'COMMUNITY':
      return _$COMMUNITY;
    case 'PUBLIC':
      return _$PUBLIC;
    default:
      throw ArgumentError(name);
  }
}

final BuiltSet<SharedWith> _$values = BuiltSet<SharedWith>(const <SharedWith>[
  _$TRUSTED,
  _$CONNECTED,
  _$COMMUNITY,
  _$PUBLIC,
]);

class _$SharedWithMeta {
  const _$SharedWithMeta();
  SharedWith get TRUSTED => _$TRUSTED;
  SharedWith get CONNECTED => _$CONNECTED;
  SharedWith get COMMUNITY => _$COMMUNITY;
  SharedWith get PUBLIC => _$PUBLIC;
  SharedWith valueOf(String name) => _$valueOf(name);
  BuiltSet<SharedWith> get values => _$values;
}

abstract class _$SharedWithMixin {
  // ignore: non_constant_identifier_names
  _$SharedWithMeta get SharedWith => const _$SharedWithMeta();
}

Serializer<SharedWith> _$sharedWithSerializer = _$SharedWithSerializer();

class _$SharedWithSerializer implements PrimitiveSerializer<SharedWith> {
  static const Map<String, Object> _toWire = const <String, Object>{
    'TRUSTED': 'TRUSTED',
    'CONNECTED': 'CONNECTED',
    'COMMUNITY': 'COMMUNITY',
    'PUBLIC': 'PUBLIC',
  };
  static const Map<Object, String> _fromWire = const <Object, String>{
    'TRUSTED': 'TRUSTED',
    'CONNECTED': 'CONNECTED',
    'COMMUNITY': 'COMMUNITY',
    'PUBLIC': 'PUBLIC',
  };

  @override
  final Iterable<Type> types = const <Type>[SharedWith];
  @override
  final String wireName = 'SharedWith';

  @override
  Object serialize(Serializers serializers, SharedWith object,
          {FullType specifiedType = FullType.unspecified}) =>
      _toWire[object.name] ?? object.name;

  @override
  SharedWith deserialize(Serializers serializers, Object serialized,
          {FullType specifiedType = FullType.unspecified}) =>
      SharedWith.valueOf(
          _fromWire[serialized] ?? (serialized is String ? serialized : ''));
}

// ignore_for_file: deprecated_member_use_from_same_package,type=lint
