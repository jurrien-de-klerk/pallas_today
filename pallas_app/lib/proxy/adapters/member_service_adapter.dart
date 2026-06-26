import 'package:built_collection/built_collection.dart';
import 'package:dio/dio.dart';
import 'package:openapi_member/openapi_member.dart' as member_api;

import '../../application/auth_service.dart';
import '../../config/api_config.dart';
import '../../domain/member.dart';
import '../../domain/member_service_port.dart';

/// Adapter that implements [MemberServicePort] via the generated Member API.
class MemberServiceAdapter implements MemberServicePort {
  MemberServiceAdapter({member_api.MembersApi? membersApi})
    : _membersApi = membersApi ?? _buildOpenapiClient().getMembersApi();

  final member_api.MembersApi _membersApi;

  @override
  Future<List<Member>> resolveMemberBatch(Set<String> memberIds) async {
    if (memberIds.isEmpty) {
      return const [];
    }

    final response = await _membersApi.getMembers(
      memberId: BuiltList<String>(memberIds.toList(growable: false)),
    );

    final members = response.data?.members;
    if (members == null || members.isEmpty) {
      return const [];
    }

    return members
        .map(
          (member) => Member(
            memberId: member.memberId,
            name: _buildDisplayName(member),
          ),
        )
        .toList(growable: false);
  }

  static member_api.OpenapiMember _buildOpenapiClient() {
    return member_api.OpenapiMember(
      // Member endpoints are service-prefixed, so the shared gateway URL works.
      basePathOverride: storyServiceBaseUrl,
      interceptors: [
        InterceptorsWrapper(
          onRequest: (options, handler) async {
            await AuthService.instance.refreshIfNeeded();
            final token = AuthService.instance.accessToken;
            if (token != null) {
              options.headers['Authorization'] = 'Bearer $token';
            }
            handler.next(options);
          },
        ),
      ],
    );
  }

  static String _buildDisplayName(member_api.Member member) {
    final displayName = '${member.firstName} ${member.lastName}'.trim();
    return displayName.isEmpty ? "Unknown Member" : displayName;
  }
}
