import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:pallas_logger/pallas_logger.dart';

import '../domain/member.dart';
import '../domain/member_repository.dart';
import '../domain/member_service_port.dart';
import '../domain/story.dart';
import '../domain/story_repository.dart';
import '../proxy/adapters/member_service_adapter.dart';
import '../proxy/adapters/stories_service_adapter.dart';

final _log = PallasLogger('StoryService');

const Object _storyServiceStateUnset = Object();

/// Application Cubit that loads stories, emits their content immediately, and
/// resolves member information as a later progressive update.
class StoryService extends Cubit<StoryServiceState> {
  StoryService({
    StoryRepository? storyRepository,
    MemberRepository? memberRepository,
    MemberServicePort? memberServicePort,
  }) : _storyRepository =
           storyRepository ??
           CachedStoryRepository(storiesServicePort: StoriesServiceAdapter()),
       _memberRepository =
           memberRepository ??
           CachedMemberRepository(
             memberServicePort: memberServicePort ?? MemberServiceAdapter(),
           ),
       super(const StoryServiceState());

  final StoryRepository _storyRepository;
  final MemberRepository _memberRepository;

  /// Temporary compatibility stub until story publishing is refactored.
  Future<bool> publishStory(dynamic document) async {
    _log.warn('publishStory is not wired into the refactor yet.');
    return false;
  }

  /// Loads a page of stories and emits the story content before resolving the
  /// related members.
  Future<void> loadStories({required int offset, required int count}) async {
    _log.debug('loadStories started.');
    _startLoadingStories(offset: offset, count: count);

    try {
      final stories = await _fetchStoriesPage(offset: offset, count: count);
      _emitStoriesLoaded(stories);

      final memberIds = _extractMemberIds(stories);
      _log.debug(
        'Extracted member IDs for story page. uniqueMemberCount=${memberIds.length}',
      );
      if (memberIds.isEmpty) {
        _stopLoadingMembers();
        _log.debug('loadStories completed without member resolution.');
        return;
      }

      await _resolveAndEmitMembers(memberIds);
      _log.debug('loadStories completed with member resolution.');
    } catch (error, stackTrace) {
      _emitLoadingFailed(error: error, stackTrace: stackTrace);
    }
  }

  void _startLoadingStories({required int offset, required int count}) {
    _log.info('Loading stories page: offset=$offset, count=$count');
    emit(
      state.copyWith(
        isLoadingStories: true,
        isLoadingMembers: false,
        error: null,
      ),
    );
  }

  Future<List<Story>> _fetchStoriesPage({
    required int offset,
    required int count,
  }) async {
    _log.debug(
      'Fetching stories from repository. offset=$offset, count=$count',
    );
    final stories = await _storyRepository.fetchStories(
      offset: offset,
      count: count,
    );
    _log.debug('Fetched stories from repository. storyCount=${stories.length}');
    return stories;
  }

  void _emitStoriesLoaded(List<Story> stories) {
    _log.debug(
      'Emitting stories-loaded state. storyCount=${stories.length}, willResolveMembers=${stories.isNotEmpty}',
    );
    emit(
      state.copyWith(
        isLoadingStories: false,
        isLoadingMembers: stories.isNotEmpty,
        stories: stories,
        membersById: const <String, Member>{},
        error: null,
      ),
    );
  }

  Set<String> _extractMemberIds(List<Story> stories) {
    if (stories.isEmpty) {
      return <String>{};
    }

    return stories.map((story) => story.authorId).toSet();
  }

  Future<void> _resolveAndEmitMembers(Set<String> memberIds) async {
    _log.debug(
      'Resolving members for story page. requestedMemberCount=${memberIds.length}',
    );
    final members = await _resolveMembers(memberIds);
    final membersById = {for (final member in members) member.memberId: member};
    _log.debug(
      'Resolved members for story page. resolvedMemberCount=${membersById.length}',
    );

    emit(state.copyWith(isLoadingMembers: false, membersById: membersById));
  }

  void _stopLoadingMembers() {
    _log.debug('No member resolution needed. Emitting loading-members=false.');
    emit(state.copyWith(isLoadingMembers: false));
  }

  void _emitLoadingFailed({
    required Object error,
    required StackTrace stackTrace,
  }) {
    _log.error('Failed to load stories page', error, stackTrace);
    _log.debug('loadStories failed. errorType=${error.runtimeType}');
    _log.backtrace();
    emit(
      state.copyWith(
        isLoadingStories: false,
        isLoadingMembers: false,
        error: error,
      ),
    );
  }

  Future<List<Member>> _resolveMembers(Set<String> memberIds) async {
    final members = await _memberRepository.getMembersByIds(memberIds);
    return members.toList(growable: false);
  }
}

class StoryServiceState {
  const StoryServiceState({
    this.isLoadingStories = false,
    this.isLoadingMembers = false,
    this.stories = const <Story>[],
    this.membersById = const <String, Member>{},
    this.error,
  });

  final bool isLoadingStories;
  final bool isLoadingMembers;
  final List<Story> stories;
  final Map<String, Member> membersById;
  final Object? error;

  bool get isInitialLoading => isLoadingStories && stories.isEmpty;

  StoryServiceState copyWith({
    bool? isLoadingStories,
    bool? isLoadingMembers,
    List<Story>? stories,
    Map<String, Member>? membersById,
    Object? error = _storyServiceStateUnset,
  }) {
    return StoryServiceState(
      isLoadingStories: isLoadingStories ?? this.isLoadingStories,
      isLoadingMembers: isLoadingMembers ?? this.isLoadingMembers,
      stories: stories ?? this.stories,
      membersById: membersById ?? this.membersById,
      error: identical(error, _storyServiceStateUnset) ? this.error : error,
    );
  }
}
