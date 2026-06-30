import 'package:built_collection/built_collection.dart';
import 'package:dio/dio.dart';
import 'package:openapi_member/openapi_member.dart' as member_api;
import 'package:pallas_logger/pallas_logger.dart';

import '../../application/auth_service.dart';
import '../../config/api_config.dart';
import '../../domain/member.dart';
import '../../domain/member_service_port.dart';

final _log = PallasLogger('MemberServiceAdapter');

/// Adapter that implements [MemberServicePort] via the generated Member API.
class MemberServiceAdapter implements MemberServicePort {
  MemberServiceAdapter({member_api.MembersApi? membersApi})
    : _membersApi = membersApi ?? _buildOpenapiClient().getMembersApi() {
    _log.debug(
      'MemberServiceAdapter initialized. baseUrl=$memberServiceBaseUrl',
    );
  }

  final member_api.MembersApi _membersApi;

  @override
  Future<List<Member>> resolveMemberBatch(Set<String> memberIds) async {
    _log.debug('Calling Members API. requestedMemberCount=${memberIds.length}');
    if (memberIds.isEmpty) {
      _log.debug('resolveMemberBatch returns early for empty request.');
      return const [];
    }

    try {
      final response = await _membersApi.getMembers(
        memberId: BuiltList<String>(memberIds.toList(growable: false)),
      );

      _log.debug(
        'Members API response received. statusCode=${response.statusCode}',
      );

      final members = response.data?.members;
      if (members == null || members.isEmpty) {
        _log.debug('Members API returned no members.');
        return const [];
      }

      final mappedMembers = members
          .map(
            (member) => Member(
              memberId: member.memberId,
              name: _buildDisplayName(member),
            ),
          )
          .toList(growable: false);
      _log.debug(
        'Members API call completed. resolvedMemberCount=${mappedMembers.length}',
      );
      return mappedMembers;
    } on DioException catch (error, stackTrace) {
      final statusCode = error.response?.statusCode;
      final hasResponseBody = error.response?.data != null;
      final requestPath = error.requestOptions.path;
      final requestMethod = error.requestOptions.method;
      final fullUri = error.requestOptions.uri.toString();
      _log.error('Members API request failed', error, stackTrace);
      _log.debug(
        'Members API failure details. '
        'requestMethod=$requestMethod, requestPath=$requestPath, fullUri=$fullUri, '
        'dioType=${error.type}, statusCode=$statusCode, hasResponseBody=$hasResponseBody',
      );
      rethrow;
    }
  }

  static member_api.OpenapiMember _buildOpenapiClient() {
    return member_api.OpenapiMember(
      basePathOverride: memberServiceBaseUrl,
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
