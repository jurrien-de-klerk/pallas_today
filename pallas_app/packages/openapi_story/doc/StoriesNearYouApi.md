# openapi_story.api.StoriesNearYouApi

## Load the API package
```dart
import 'package:openapi_story/api.dart';
```

All URIs are relative to *http://localhost:8080*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getStoriesNearYou**](StoriesNearYouApi.md#getstoriesnearyou) | **GET** /stories/near/{memberId} | Get Stories near you for a member


# **getStoriesNearYou**
> StoriesNearYouPage getStoriesNearYou(memberId, limit, before)

Get Stories near you for a member

Returns the Stories near you feed for the member identified by `memberId`. The feed aggregates stories published by that member, by members in their trusted circle, and by members in their connected circle, ordered by publication time descending (newest first). When a requesting member browses another member's feed, stories are filtered by their Shared With level: only stories visible to the requesting member are included. Stories the requesting member is not permitted to see are silently excluded.

### Example
```dart
import 'package:openapi_story/api.dart';
// TODO Configure OAuth2 access token for authorization: oauth2
//defaultApiClient.getAuthentication<OAuth>('oauth2').accessToken = 'YOUR_ACCESS_TOKEN';

final api = OpenapiStory().getStoriesNearYouApi();
final String memberId = 38400000-8cf0-11bd-b23e-10b96e4ef00d; // String | The ID of the member whose Stories near you feed is being requested. Use the authenticated member's own ID to get their personal feed.
final int limit = 56; // int | Maximum number of stories to return
final DateTime before = 2013-10-20T19:20:30+01:00; // DateTime | Return only stories published before this timestamp (ISO 8601). Use for cursor-based pagination: pass the `publishedAt` value of the last story received to fetch the next page.

try {
    final response = api.getStoriesNearYou(memberId, limit, before);
    print(response);
} catch on DioException (e) {
    print('Exception when calling StoriesNearYouApi->getStoriesNearYou: $e\n');
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **memberId** | **String**| The ID of the member whose Stories near you feed is being requested. Use the authenticated member's own ID to get their personal feed. | 
 **limit** | **int**| Maximum number of stories to return | [optional] [default to 20]
 **before** | **DateTime**| Return only stories published before this timestamp (ISO 8601). Use for cursor-based pagination: pass the `publishedAt` value of the last story received to fetch the next page. | [optional] 

### Return type

[**StoriesNearYouPage**](StoriesNearYouPage.md)

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

