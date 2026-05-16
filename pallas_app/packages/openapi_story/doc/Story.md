# openapi_story.model.Story

## Load the model package
```dart
import 'package:openapi_story/api.dart';
```

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | **String** | Unique identifier of the story | 
**authorId** | **String** | The memberId of the member who published the story | 
**content** | **String** | The story body as a JSON-serialised Quill Delta array of insert operations. Image references within the Delta are URLs pointing to file storage; raw image data is never embedded. | 
**sharedWith** | [**SharedWith**](SharedWith.md) |  | 
**publishedAt** | [**DateTime**](DateTime.md) | The date and time at which the story was published (ISO 8601, UTC). Used to order stories in the Stories near you feed. | 

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)


