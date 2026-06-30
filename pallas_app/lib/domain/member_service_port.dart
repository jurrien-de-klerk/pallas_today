import 'member.dart';

/// Port interface for fetching members from the Member Service.
///
/// This is a driven port (provided interface) that defines the contract
/// for fetching member information from the member service.
abstract interface class MemberServicePort {
  /// Resolves a batch of member IDs into [Member] objects.
  ///
  /// Takes a set of unique member IDs (UUIDs) and returns the members that
  /// could be resolved.
  /// Throws an exception if the resolution fails.
  Future<List<Member>> resolveMemberBatch(Set<String> memberIds);
}
