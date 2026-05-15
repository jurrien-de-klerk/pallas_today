//
// AUTO-GENERATED FILE, DO NOT MODIFY!
//

// ignore_for_file: unused_element
import 'package:built_collection/built_collection.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';

part 'shared_with.g.dart';

class SharedWith extends EnumClass {
  /// The audience permitted to read a story. - TRUSTED: members in the author's trusted circle (default). - CONNECTED: members in the author's connected circle. - COMMUNITY: all registered members of the Pallas Community. - PUBLIC: everyone, including guests.
  @BuiltValueEnumConst(wireName: r'TRUSTED')
  static const SharedWith TRUSTED = _$TRUSTED;

  /// The audience permitted to read a story. - TRUSTED: members in the author's trusted circle (default). - CONNECTED: members in the author's connected circle. - COMMUNITY: all registered members of the Pallas Community. - PUBLIC: everyone, including guests.
  @BuiltValueEnumConst(wireName: r'CONNECTED')
  static const SharedWith CONNECTED = _$CONNECTED;

  /// The audience permitted to read a story. - TRUSTED: members in the author's trusted circle (default). - CONNECTED: members in the author's connected circle. - COMMUNITY: all registered members of the Pallas Community. - PUBLIC: everyone, including guests.
  @BuiltValueEnumConst(wireName: r'COMMUNITY')
  static const SharedWith COMMUNITY = _$COMMUNITY;

  /// The audience permitted to read a story. - TRUSTED: members in the author's trusted circle (default). - CONNECTED: members in the author's connected circle. - COMMUNITY: all registered members of the Pallas Community. - PUBLIC: everyone, including guests.
  @BuiltValueEnumConst(wireName: r'PUBLIC')
  static const SharedWith PUBLIC = _$PUBLIC;

  static Serializer<SharedWith> get serializer => _$sharedWithSerializer;

  const SharedWith._(String name) : super(name);

  static BuiltSet<SharedWith> get values => _$values;
  static SharedWith valueOf(String name) => _$valueOf(name);
}

/// Optionally, enum_class can generate a mixin to go with your enum for use
/// with Angular. It exposes your enum constants as getters. So, if you mix it
/// in to your Dart component class, the values become available to the
/// corresponding Angular template.
///
/// Trigger mixin generation by writing a line like this one next to your enum.
abstract class SharedWithMixin = Object with _$SharedWithMixin;
