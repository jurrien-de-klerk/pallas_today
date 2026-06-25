# openapi_story.api.StoriesNearMeApi

## Load the API package
```dart
import 'package:openapi_story/api.dart';
```

All URIs are relative to *http://localhost:8080*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getStoriesNearMe**](StoriesNearMeApi.md#getstoriesnearme) | **GET** /stories/near-me | Get your Stories near you feed


# **getStoriesNearMe**
> StoriesPage getStoriesNearMe(offset, count)

Get your Stories near you feed

Returns your personal Stories near you feed, aggregating stories published by you, by members in your trusted circle, and by members in your connected circle, ordered by publication time descending (newest first). Uses offset-based pagination for infinite scrolling.

### Example
```dart
import 'package:openapi_story/api.dart';
// TODO Configure OAuth2 access token for authorization: oauth2
//defaultApiClient.getAuthentication<OAuth>('oauth2').accessToken = 'YOUR_ACCESS_TOKEN';

final api = OpenapiStory().getStoriesNearMeApi();
final int offset = 56; // int | Number of stories to skip (for pagination)
final int count = 56; // int | Maximum number of stories to return

try {
    final response = api.getStoriesNearMe(offset, count);
    print(response);
} catch on DioException (e) {
    print('Exception when calling StoriesNearMeApi->getStoriesNearMe: $e\n');
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **offset** | **int**| Number of stories to skip (for pagination) | [optional] [default to 0]
 **count** | **int**| Maximum number of stories to return | [optional] [default to 20]

### Return type

[**StoriesPage**](StoriesPage.md)

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

