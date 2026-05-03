# openapi.api.StoriesApi

## Load the API package
```dart
import 'package:openapi/api.dart';
```

All URIs are relative to *http://localhost:8080*

Method | HTTP request | Description
------------- | ------------- | -------------
[**createStory**](StoriesApi.md#createstory) | **POST** /stories | Create a new story
[**deleteStory**](StoriesApi.md#deletestory) | **DELETE** /stories/{id} | Delete a story
[**getStory**](StoriesApi.md#getstory) | **GET** /stories/{id} | Get a story by ID
[**listStories**](StoriesApi.md#liststories) | **GET** /stories | List all stories
[**updateStory**](StoriesApi.md#updatestory) | **PUT** /stories/{id} | Update a story


# **createStory**
> Story createStory(storyInput)

Create a new story

Create a new story with the provided content

### Example
```dart
import 'package:openapi/api.dart';
// TODO Configure OAuth2 access token for authorization: oauth2
//defaultApiClient.getAuthentication<OAuth>('oauth2').accessToken = 'YOUR_ACCESS_TOKEN';

final api = Openapi().getStoriesApi();
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
> deleteStory(id)

Delete a story

Delete a specific story by its unique identifier

### Example
```dart
import 'package:openapi/api.dart';
// TODO Configure OAuth2 access token for authorization: oauth2
//defaultApiClient.getAuthentication<OAuth>('oauth2').accessToken = 'YOUR_ACCESS_TOKEN';

final api = Openapi().getStoriesApi();
final String id = 38400000-8cf0-11bd-b23e-10b96e4ef00d; // String | Story ID

try {
    api.deleteStory(id);
} catch on DioException (e) {
    print('Exception when calling StoriesApi->deleteStory: $e\n');
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**| Story ID | 

### Return type

void (empty response body)

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getStory**
> Story getStory(id)

Get a story by ID

Retrieve a specific story by its unique identifier

### Example
```dart
import 'package:openapi/api.dart';
// TODO Configure OAuth2 access token for authorization: oauth2
//defaultApiClient.getAuthentication<OAuth>('oauth2').accessToken = 'YOUR_ACCESS_TOKEN';

final api = Openapi().getStoriesApi();
final String id = 38400000-8cf0-11bd-b23e-10b96e4ef00d; // String | Story ID

try {
    final response = api.getStory(id);
    print(response);
} catch on DioException (e) {
    print('Exception when calling StoriesApi->getStory: $e\n');
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**| Story ID | 

### Return type

[**Story**](Story.md)

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **listStories**
> ListStories200Response listStories(limit, offset)

List all stories

Retrieve a list of all stories

### Example
```dart
import 'package:openapi/api.dart';
// TODO Configure OAuth2 access token for authorization: oauth2
//defaultApiClient.getAuthentication<OAuth>('oauth2').accessToken = 'YOUR_ACCESS_TOKEN';

final api = Openapi().getStoriesApi();
final int limit = 56; // int | Maximum number of stories to return
final int offset = 56; // int | Number of stories to skip

try {
    final response = api.listStories(limit, offset);
    print(response);
} catch on DioException (e) {
    print('Exception when calling StoriesApi->listStories: $e\n');
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **limit** | **int**| Maximum number of stories to return | [optional] [default to 20]
 **offset** | **int**| Number of stories to skip | [optional] [default to 0]

### Return type

[**ListStories200Response**](ListStories200Response.md)

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **updateStory**
> Story updateStory(id, storyInput)

Update a story

Update an existing story with new content

### Example
```dart
import 'package:openapi/api.dart';
// TODO Configure OAuth2 access token for authorization: oauth2
//defaultApiClient.getAuthentication<OAuth>('oauth2').accessToken = 'YOUR_ACCESS_TOKEN';

final api = Openapi().getStoriesApi();
final String id = 38400000-8cf0-11bd-b23e-10b96e4ef00d; // String | Story ID
final StoryInput storyInput = ; // StoryInput | 

try {
    final response = api.updateStory(id, storyInput);
    print(response);
} catch on DioException (e) {
    print('Exception when calling StoriesApi->updateStory: $e\n');
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**| Story ID | 
 **storyInput** | [**StoryInput**](StoryInput.md)|  | 

### Return type

[**Story**](Story.md)

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

