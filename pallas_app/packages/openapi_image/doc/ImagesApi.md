# openapi.api.ImagesApi

## Load the API package
```dart
import 'package:openapi/api.dart';
```

All URIs are relative to *http://localhost:8081*

Method | HTTP request | Description
------------- | ------------- | -------------
[**addImage**](ImagesApi.md#addimage) | **POST** /images/story/{storyId} | Upload an image for a story
[**getImage**](ImagesApi.md#getimage) | **GET** /images/story/{storyId}/{imageId} | Get an image
[**removeImage**](ImagesApi.md#removeimage) | **DELETE** /images/story/{storyId}/{imageId} | Remove an image


# **addImage**
> Image addImage(storyId, file)

Upload an image for a story

Upload a new image and associate it with the specified story.

### Example
```dart
import 'package:openapi/api.dart';
// TODO Configure OAuth2 access token for authorization: oauth2
//defaultApiClient.getAuthentication<OAuth>('oauth2').accessToken = 'YOUR_ACCESS_TOKEN';

final api = Openapi().getImagesApi();
final String storyId = 38400000-8cf0-11bd-b23e-10b96e4ef00d; // String | The ID of the story that owns this image
final MultipartFile file = BINARY_DATA_HERE; // MultipartFile | The image file to upload. Supported formats: JPEG, PNG, WebP. Maximum file size: 10 MB.

try {
    final response = api.addImage(storyId, file);
    print(response);
} catch on DioException (e) {
    print('Exception when calling ImagesApi->addImage: $e\n');
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **storyId** | **String**| The ID of the story that owns this image | 
 **file** | **MultipartFile**| The image file to upload. Supported formats: JPEG, PNG, WebP. Maximum file size: 10 MB. | 

### Return type

[**Image**](Image.md)

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: multipart/form-data
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getImage**
> Uint8List getImage(storyId, imageId)

Get an image

Download the image file belonging to the specified story.

### Example
```dart
import 'package:openapi/api.dart';
// TODO Configure OAuth2 access token for authorization: oauth2
//defaultApiClient.getAuthentication<OAuth>('oauth2').accessToken = 'YOUR_ACCESS_TOKEN';

final api = Openapi().getImagesApi();
final String storyId = 38400000-8cf0-11bd-b23e-10b96e4ef00d; // String | The ID of the story that owns this image
final String imageId = 38400000-8cf0-11bd-b23e-10b96e4ef00d; // String | The ID of the image to download

try {
    final response = api.getImage(storyId, imageId);
    print(response);
} catch on DioException (e) {
    print('Exception when calling ImagesApi->getImage: $e\n');
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **storyId** | **String**| The ID of the story that owns this image | 
 **imageId** | **String**| The ID of the image to download | 

### Return type

[**Uint8List**](Uint8List.md)

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: image/jpeg, image/png, image/webp, application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **removeImage**
> removeImage(storyId, imageId)

Remove an image

Delete an image belonging to the specified story from file storage.

### Example
```dart
import 'package:openapi/api.dart';
// TODO Configure OAuth2 access token for authorization: oauth2
//defaultApiClient.getAuthentication<OAuth>('oauth2').accessToken = 'YOUR_ACCESS_TOKEN';

final api = Openapi().getImagesApi();
final String storyId = 38400000-8cf0-11bd-b23e-10b96e4ef00d; // String | The ID of the story that owns this image
final String imageId = 38400000-8cf0-11bd-b23e-10b96e4ef00d; // String | The ID of the image to delete

try {
    api.removeImage(storyId, imageId);
} catch on DioException (e) {
    print('Exception when calling ImagesApi->removeImage: $e\n');
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **storyId** | **String**| The ID of the story that owns this image | 
 **imageId** | **String**| The ID of the image to delete | 

### Return type

void (empty response body)

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

