// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'serializers.dart';

// **************************************************************************
// BuiltValueGenerator
// **************************************************************************

Serializers _$serializers = (Serializers().toBuilder()
      ..add(Error.serializer)
      ..add(QuillDeltaOperation.serializer)
      ..add(QuillDeltaOperationInsert.serializer)
      ..add(SharedWith.serializer)
      ..add(StoriesPage.serializer)
      ..add(Story.serializer)
      ..add(StoryInput.serializer)
      ..addBuilderFactory(
          const FullType(BuiltList, const [const FullType(Story)]),
          () => ListBuilder<Story>())
      ..addBuilderFactory(
          const FullType(BuiltList, const [const FullType(dynamic)]),
          () => ListBuilder<dynamic>())
      ..addBuilderFactory(
          const FullType(BuiltList, const [const FullType(dynamic)]),
          () => ListBuilder<dynamic>())
      ..addBuilderFactory(
          const FullType(BuiltMap, const [
            const FullType(String),
            const FullType.nullable(JsonObject)
          ]),
          () => MapBuilder<String, JsonObject?>()))
    .build();

// ignore_for_file: deprecated_member_use_from_same_package,type=lint
