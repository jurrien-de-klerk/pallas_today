# openapi_story.api.StoriesApi

## Load the API package
```dart
import 'package:openapi_story/api.dart';
```

All URIs are relative to *http://localhost:8080*

Method | HTTP request | Description
------------- | ------------- | -------------
[**createStory**](StoriesApi.md#createstory) | **POST** /stories | Publish a story
[**deleteStory**](StoriesApi.md#deletestory) | **DELETE** /stories/{storyId} | Delete a story
[**getStory**](StoriesApi.md#getstory) | **GET** /stories/{storyId} | Get a story
[**updateStory**](StoriesApi.md#updatestory) | **PUT** /stories/{storyId} | Update a story


# **createStory**
> Story createStory(storyInput)

Publish a story

Create a new story for the authenticated member. The caller must supply the story content as a JSON Delta string and choose a Shared With level. Defaults to TRUSTED when no level is provided.

### Example
```dart
import 'package:openapi_story/api.dart';
// TODO Configure OAuth2 access token for authorization: oauth2
//defaultApiClient.getAuthentication<OAuth>('oauth2').accessToken = 'YOUR_ACCESS_TOKEN';

final api = OpenapiStory().getStoriesApi();
final StoryInput storyInput = ; // StoryInput | 

try {
    final response = api.createStory(storyInput);
    print(response);
} catch on DioException (e) {
    print('Exception when calling StoriesApi->createStory: $e\n');
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **storyInput** | [**StoryInput**](StoryInput.md)|  | 

### Return type

[**Story**](Story.md)

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **deleteStory**
> deleteStory(storyId)

Delete a story

Delete a story and all images associated with it. Only the author of the story may delete it. If the story exists but the requesting member is not the author, a 404 is returned so as not to reveal the story's existence to non-authors.

### Example
```dart
import 'package:openapi_story/api.dart';
// TODO Configure OAuth2 access token for authorization: oauth2
//defaultApiClient.getAuthentication<OAuth>('oauth2').accessToken = 'YOUR_ACCESS_TOKEN';

final api = OpenapiStory().getStoriesApi();
final String storyId = 38400000-8cf0-11bd-b23e-10b96e4ef00d; // String | Unique identifier of the story

try {
    api.deleteStory(storyId);
} catch on DioException (e) {
    print('Exception when calling StoriesApi->deleteStory: $e\n');
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **storyId** | **String**| Unique identifier of the story | 

### Return type

void (empty response body)

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getStory**
> Story getStory(storyId)

Get a story

Retrieve a specific story by its ID. The story is only returned when the requesting member's relationship with the author satisfies the story's Shared With level. If the story exists but the member is not permitted to see it, a 404 is returned so as not to reveal its existence.

### Example
```dart
import 'package:openapi_story/api.dart';
// TODO Configure OAuth2 access token for authorization: oauth2
//defaultApiClient.getAuthentication<OAuth>('oauth2').accessToken = 'YOUR_ACCESS_TOKEN';

final api = OpenapiStory().getStoriesApi();
final String storyId = 38400000-8cf0-11bd-b23e-10b96e4ef00d; // String | Unique identifier of the story

try {
    final response = api.getStory(storyId);
    print(response);
} catch on DioException (e) {
    print('Exception when calling StoriesApi->getStory: $e\n');
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **storyId** | **String**| Unique identifier of the story | 

### Return type

[**Story**](Story.md)

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **updateStory**
> Story updateStory(storyId, storyInput)

Update a story

Update the content or Shared With level of an existing story. Only the author of the story may update it. If the story exists but the requesting member is not the author, a 404 is returned so as not to reveal the story's existence to non-authors.

### Example
```dart
import 'package:openapi_story/api.dart';
// TODO Configure OAuth2 access token for authorization: oauth2
//defaultApiClient.getAuthentication<OAuth>('oauth2').accessToken = 'YOUR_ACCESS_TOKEN';

final api = OpenapiStory().getStoriesApi();
final String storyId = 38400000-8cf0-11bd-b23e-10b96e4ef00d; // String | Unique identifier of the story
final StoryInput storyInput = ; // StoryInput | 

try {
    final response = api.updateStory(storyId, storyInput);
    print(response);
} catch on DioException (e) {
    print('Exception when calling StoriesApi->updateStory: $e\n');
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **storyId** | **String**| Unique identifier of the story | 
 **storyInput** | [**StoryInput**](StoryInput.md)|  | 

### Return type

[**Story**](Story.md)

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

