# openapi_story.model.StoriesNearYouPage

## Load the model package
```dart
import 'package:openapi_story/api.dart';
```

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**stories** | [**BuiltList&lt;Story&gt;**](Story.md) | Stories ordered by publishedAt descending. Only stories visible to the requesting member are included. | 
**nextBefore** | [**DateTime**](DateTime.md) | Pass this value as the `before` query parameter to fetch the next page. Absent when there are no more stories to return. | [optional] 

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)


