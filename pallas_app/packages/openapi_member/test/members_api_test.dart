import 'package:test/test.dart';
import 'package:openapi_member/openapi_member.dart';

/// tests for MembersApi
void main() {
  final instance = OpenapiMember().getMembersApi();

  group(MembersApi, () {
    // Get a member
    //
    // Retrieve a single member by their user ID. Returns first and last name only. Returns 404 when no member with the given ID exists.
    //
    //Future<Member> getMember(String userId) async
    test('test getMember', () async {
      // TODO
    });

    // Get a batch of members
    //
    // Retrieve multiple members by their user IDs in a single request. IDs are supplied as repeated `userId` query parameters. Members not found for a given ID are silently omitted from the response; no error is returned for unknown IDs.
    //
    //Future<MemberBatch> getMembers(BuiltList<String> userId) async
    test('test getMembers', () async {
      // TODO
    });
  });
}
