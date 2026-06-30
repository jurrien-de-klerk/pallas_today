import 'package:pallas_logger/pallas_logger.dart';

import 'member.dart';
import 'member_service_port.dart';

final _log = PallasLogger('CachedMemberRepository');

/// Domain repository for retrieving members by their IDs.
abstract interface class MemberRepository {
  /// Returns all resolvable members for the requested member IDs.
  Future<Set<Member>> getMembersByIds(Set<String> memberIds);

  /// Drops all cached members.
  void clearCache();
}

/// Member repository that caches members by member ID.
class CachedMemberRepository implements MemberRepository {
  CachedMemberRepository({required MemberServicePort memberServicePort})
    : _memberServicePort = memberServicePort;

  final MemberServicePort _memberServicePort;
  final Map<String, Member> _cacheById = <String, Member>{};

  @override
  Future<Set<Member>> getMembersByIds(Set<String> memberIds) async {
    _log.debug(
      'getMembersByIds called. requestedMemberCount=${memberIds.length}',
    );
    if (memberIds.isEmpty) {
      _log.debug('getMembersByIds returns early for empty request.');
      return <Member>{};
    }

    final missingIds = memberIds
        .where((memberId) => !_cacheById.containsKey(memberId))
        .toSet();
    _log.debug(
      'Member cache lookup complete. missingMemberCount=${missingIds.length}, cacheSize=${_cacheById.length}',
    );

    if (missingIds.isNotEmpty) {
      _log.debug('Resolving missing members via port.');
      final resolvedMembers = await _memberServicePort.resolveMemberBatch(
        missingIds,
      );
      for (final member in resolvedMembers) {
        _cacheById[member.memberId] = member;
      }
      _log.debug(
        'Missing members resolved. resolvedMemberCount=${resolvedMembers.length}, cacheSize=${_cacheById.length}',
      );
    }

    final result = memberIds
        .map((memberId) => _cacheById[memberId])
        .whereType<Member>()
        .toSet();
    _log.debug(
      'getMembersByIds completed. returnedMemberCount=${result.length}',
    );
    return result;
  }

  @override
  void clearCache() {
    _log.debug('clearCache called. previousCacheSize=${_cacheById.length}');
    _cacheById.clear();
  }
}
