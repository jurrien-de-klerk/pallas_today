/// Represents a member in the domain layer.
class Member {
  /// Unique identifier for the member (UUID string).
  final String memberId;

  /// The display name of the member.
  final String name;

  const Member({required this.memberId, required this.name});

  Member copyWith({String? memberId, String? name}) {
    return Member(memberId: memberId ?? this.memberId, name: name ?? this.name);
  }

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is Member &&
          runtimeType == other.runtimeType &&
          memberId == other.memberId &&
          name == other.name;

  @override
  int get hashCode => Object.hash(memberId, name);

  @override
  String toString() => 'Member(memberId: $memberId, name: $name)';
}
