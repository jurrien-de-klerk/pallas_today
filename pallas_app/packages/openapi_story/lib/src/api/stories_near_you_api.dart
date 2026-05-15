//
// AUTO-GENERATED FILE, DO NOT MODIFY!
//

import 'dart:async';

import 'package:built_value/json_object.dart';
import 'package:built_value/serializer.dart';
import 'package:dio/dio.dart';

import 'package:openapi_story/src/api_util.dart';
import 'package:openapi_story/src/model/error.dart';
import 'package:openapi_story/src/model/stories_near_you_page.dart';

class StoriesNearYouApi {
  final Dio _dio;

  final Serializers _serializers;

  const StoriesNearYouApi(this._dio, this._serializers);

  /// Get Stories near you for a member
  /// Returns the Stories near you feed for the member identified by &#x60;memberId&#x60;. The feed aggregates stories published by that member, by members in their trusted circle, and by members in their connected circle, ordered by publication time descending (newest first). When a requesting member browses another member&#39;s feed, stories are filtered by their Shared With level: only stories visible to the requesting member are included. Stories the requesting member is not permitted to see are silently excluded.
  ///
  /// Parameters:
  /// * [memberId] - The ID of the member whose Stories near you feed is being requested. Use the authenticated member's own ID to get their personal feed.
  /// * [limit] - Maximum number of stories to return
  /// * [before] - Return only stories published before this timestamp (ISO 8601). Use for cursor-based pagination: pass the `publishedAt` value of the last story received to fetch the next page.
  /// * [cancelToken] - A [CancelToken] that can be used to cancel the operation
  /// * [headers] - Can be used to add additional headers to the request
  /// * [extras] - Can be used to add flags to the request
  /// * [validateStatus] - A [ValidateStatus] callback that can be used to determine request success based on the HTTP status of the response
  /// * [onSendProgress] - A [ProgressCallback] that can be used to get the send progress
  /// * [onReceiveProgress] - A [ProgressCallback] that can be used to get the receive progress
  ///
  /// Returns a [Future] containing a [Response] with a [StoriesNearYouPage] as data
  /// Throws [DioException] if API call or serialization fails
  Future<Response<StoriesNearYouPage>> getStoriesNearYou({
    required String memberId,
    int? limit = 20,
    DateTime? before,
    CancelToken? cancelToken,
    Map<String, dynamic>? headers,
    Map<String, dynamic>? extra,
    ValidateStatus? validateStatus,
    ProgressCallback? onSendProgress,
    ProgressCallback? onReceiveProgress,
  }) async {
    final _path = r'/stories/near/{memberId}'.replaceAll(
        '{' r'memberId' '}',
        encodeQueryParameter(_serializers, memberId, const FullType(String))
            .toString());
    final _options = Options(
      method: r'GET',
      headers: <String, dynamic>{
        ...?headers,
      },
      extra: <String, dynamic>{
        'secure': <Map<String, String>>[
          {
            'type': 'oauth2',
            'name': 'oauth2',
          },
        ],
        ...?extra,
      },
      validateStatus: validateStatus,
    );

    final _queryParameters = <String, dynamic>{
      if (limit != null)
        r'limit':
            encodeQueryParameter(_serializers, limit, const FullType(int)),
      if (before != null)
        r'before': encodeQueryParameter(
            _serializers, before, const FullType(DateTime)),
    };

    final _response = await _dio.request<Object>(
      _path,
      options: _options,
      queryParameters: _queryParameters,
      cancelToken: cancelToken,
      onSendProgress: onSendProgress,
      onReceiveProgress: onReceiveProgress,
    );

    StoriesNearYouPage? _responseData;

    try {
      final rawResponse = _response.data;
      _responseData = rawResponse == null
          ? null
          : _serializers.deserialize(
              rawResponse,
              specifiedType: const FullType(StoriesNearYouPage),
            ) as StoriesNearYouPage;
    } catch (error, stackTrace) {
      throw DioException(
        requestOptions: _response.requestOptions,
        response: _response,
        type: DioExceptionType.unknown,
        error: error,
        stackTrace: stackTrace,
      );
    }

    return Response<StoriesNearYouPage>(
      data: _responseData,
      headers: _response.headers,
      isRedirect: _response.isRedirect,
      requestOptions: _response.requestOptions,
      redirects: _response.redirects,
      statusCode: _response.statusCode,
      statusMessage: _response.statusMessage,
      extra: _response.extra,
    );
  }
}
