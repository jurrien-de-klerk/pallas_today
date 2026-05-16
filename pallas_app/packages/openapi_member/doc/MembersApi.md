# openapi_member.api.MembersApi

## Load the API package
```dart
import 'package:openapi_member/api.dart';
```

All URIs are relative to *http://localhost:8081*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAuthenticatedMember**](MembersApi.md#getauthenticatedmember) | **GET** /members/me | Get the authenticated member&#39;s own profile
[**getMember**](MembersApi.md#getmember) | **GET** /members/{userId} | Get a member
[**getMembers**](MembersApi.md#getmembers) | **GET** /members/batch | Get a batch of members


# **getAuthenticatedMember**
> Member getAuthenticatedMember()

Get the authenticated member's own profile

Retrieve the profile of the currently authenticated member. The member is identified from the access token's `sub` claim. Returns first and last name only.

### Example
```dart
import 'package:openapi_member/api.dart';
// TODO Configure OAuth2 access token for authorization: oauth2
//defaultApiClient.getAuthentication<OAuth>('oauth2').accessToken = 'YOUR_ACCESS_TOKEN';

final api = OpenapiMember().getMembersApi();

try {
    final response = api.getAuthenticatedMember();
    print(response);
} catch on DioException (e) {
    print('Exception when calling MembersApi->getAuthenticatedMember: $e\n');
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**Member**](Member.md)

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getMember**
> Member getMember(userId)

Get a member

Retrieve a single member by their user ID. Returns first and last name only. Returns 404 when no member with the given ID exists.

### Example
```dart
import 'package:openapi_member/api.dart';
// TODO Configure OAuth2 access token for authorization: oauth2
//defaultApiClient.getAuthentication<OAuth>('oauth2').accessToken = 'YOUR_ACCESS_TOKEN';

final api = OpenapiMember().getMembersApi();
final String userId = 987fcdeb-51a2-43d7-b012-345678901234; // String | The unique identifier of the member (OIDC sub claim).

try {
    final response = api.getMember(userId);
    print(response);
} catch on DioException (e) {
    print('Exception when calling MembersApi->getMember: $e\n');
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **userId** | **String**| The unique identifier of the member (OIDC sub claim). | 

### Return type

[**Member**](Member.md)

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getMembers**
> MemberBatch getMembers(userId)

Get a batch of members

Retrieve multiple members by their user IDs in a single request. IDs are supplied as repeated `userId` query parameters. Members not found for a given ID are silently omitted from the response; no error is returned for unknown IDs.

### Example
```dart
import 'package:openapi_member/api.dart';
// TODO Configure OAuth2 access token for authorization: oauth2
//defaultApiClient.getAuthentication<OAuth>('oauth2').accessToken = 'YOUR_ACCESS_TOKEN';

final api = OpenapiMember().getMembersApi();
final BuiltList<String> userId = ["987fcdeb-51a2-43d7-b012-345678901234","123e4567-e89b-12d3-a456-426614174000"]; // BuiltList<String> | One or more user IDs to look up. Repeat this parameter for each requested user ID.

try {
    final response = api.getMembers(userId);
    print(response);
} catch on DioException (e) {
    print('Exception when calling MembersApi->getMembers: $e\n');
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **userId** | [**BuiltList&lt;String&gt;**](String.md)| One or more user IDs to look up. Repeat this parameter for each requested user ID. | 

### Return type

[**MemberBatch**](MemberBatch.md)

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

