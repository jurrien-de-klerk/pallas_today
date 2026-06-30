import 'package:flutter_test/flutter_test.dart';
import 'package:pallas_app/domain/member.dart';
import 'package:pallas_app/domain/member_repository.dart';
import 'package:pallas_app/domain/member_service_port.dart';

void main() {
  group('CachedMemberRepository', () {
    test('resolves only missing IDs and merges with cache', () async {
      final memberA = _member('a');
      final memberB = _member('b');
      final memberC = _member('c');
      final port = _FakeMemberServicePort(
        responsesByIds: {
          {'a', 'b'}: [memberA, memberB],
          {'c'}: [memberC],
        },
      );
      final repository = CachedMemberRepository(memberServicePort: port);

      final firstResult = await repository.getMembersByIds({'a', 'b'});
      final secondResult = await repository.getMembersByIds({'b', 'c'});

      expect(firstResult, {memberA, memberB});
      expect(secondResult, {memberB, memberC});
      expect(port.calls, [
        {'a', 'b'},
        {'c'},
      ]);
    });

    test(
      'does not call member service when all requested IDs are cached',
      () async {
        final memberA = _member('a');
        final port = _FakeMemberServicePort(
          responsesByIds: {
            {'a'}: [memberA],
          },
        );
        final repository = CachedMemberRepository(memberServicePort: port);

        await repository.getMembersByIds({'a'});
        final fromCache = await repository.getMembersByIds({'a'});

        expect(fromCache, {memberA});
        expect(port.calls, [
          {'a'},
        ]);
      },
    );

    test(
      'returns only resolvable members when service omits some IDs',
      () async {
        final memberA = _member('a');
        final port = _FakeMemberServicePort(
          responsesByIds: {
            {'a', 'missing'}: [memberA],
          },
        );
        final repository = CachedMemberRepository(memberServicePort: port);

        final members = await repository.getMembersByIds({'a', 'missing'});

        expect(members, {memberA});
        expect(port.calls, [
          {'a', 'missing'},
        ]);
      },
    );

    test('clearCache removes cached members', () async {
      final memberA = _member('a');
      final port = _FakeMemberServicePort(
        responsesByIds: {
          {'a'}: [memberA],
        },
      );
      final repository = CachedMemberRepository(memberServicePort: port);

      await repository.getMembersByIds({'a'});
      repository.clearCache();
      await repository.getMembersByIds({'a'});

      expect(port.calls, [
        {'a'},
        {'a'},
      ]);
    });
  });
}

Member _member(String id) => Member(memberId: id, name: 'Member $id');

class _FakeMemberServicePort implements MemberServicePort {
  _FakeMemberServicePort({required this.responsesByIds});

  final Map<Set<String>, List<Member>> responsesByIds;
  final List<Set<String>> calls = <Set<String>>[];

  @override
  Future<List<Member>> resolveMemberBatch(Set<String> memberIds) async {
    calls.add(memberIds);

    for (final entry in responsesByIds.entries) {
      if (_setEquals(entry.key, memberIds)) {
        return entry.value;
      }
    }

    return const <Member>[];
  }

  bool _setEquals(Set<String> left, Set<String> right) {
    if (left.length != right.length) {
      return false;
    }

    return left.containsAll(right);
  }
}
