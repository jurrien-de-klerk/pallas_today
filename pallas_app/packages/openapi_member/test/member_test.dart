import 'package:test/test.dart';
import 'package:openapi_member/openapi_member.dart';

// tests for Member
void main() {
  final instance = MemberBuilder();
  // TODO add properties to the builder and call build()

  group(Member, () {
    // The unique identifier of the member. Corresponds to the OIDC `sub` claim issued by Keycloak.
    // String userId
    test('to test the property `userId`', () async {
      // TODO
    });

    // The member's given name.
    // String firstName
    test('to test the property `firstName`', () async {
      // TODO
    });

    // The member's family name.
    // String lastName
    test('to test the property `lastName`', () async {
      // TODO
    });
  });
}
