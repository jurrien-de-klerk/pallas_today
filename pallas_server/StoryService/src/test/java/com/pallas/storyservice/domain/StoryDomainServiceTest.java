package com.pallas.storyservice.domain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("StoryDomainService")
class StoryDomainServiceTest {

  @Mock private StoryPort storyPort;
  @Mock private CommunityServicePort communityServicePort;

  private StoryDomainService service;

  @BeforeEach
  void setUp() {
    service = new StoryDomainService(storyPort, communityServicePort);
  }

  // =========================================================================
  // createStory tests
  // =========================================================================

  @Test
  @DisplayName("createStory should persist story and return it with generated id and timestamp")
  void testCreateStoryPersists() {
    // Arrange
    UUID authorId = UUID.randomUUID();
    String content = "My first story";
    SharedWith sharedWith = SharedWith.PUBLIC;

    ArgumentCaptor<Story> storyCaptor = ArgumentCaptor.forClass(Story.class);

    Story persisted =
        new Story(UUID.randomUUID(), authorId, content, sharedWith, OffsetDateTime.now());
    when(storyPort.save(any(Story.class))).thenReturn(persisted);

    // Act
    Story result = service.createStory(authorId, content, sharedWith);

    // Assert
    assertEquals(persisted, result);
    assertEquals(authorId, result.getAuthorId());
    assertEquals(content, result.getContent());
    assertEquals(sharedWith, result.getSharedWith());
    assertNotNull(result.getId());
    assertNotNull(result.getPublishedAt());

    // Verify port was called with story
    verify(storyPort).save(storyCaptor.capture());
    Story savedStory = storyCaptor.getValue();
    assertEquals(authorId, savedStory.getAuthorId());
    assertEquals(content, savedStory.getContent());
    assertEquals(sharedWith, savedStory.getSharedWith());
  }

  // =========================================================================
  // getStory - author access tests
  // =========================================================================

  @Test
  @DisplayName("getStory should allow author to view their own story")
  void testGetStoryAuthorCanView() {
    // Arrange
    UUID storyId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    UUID requesterId = authorId; // Same person

    Story story = new Story(storyId, authorId, "content", SharedWith.TRUSTED, OffsetDateTime.now());
    when(storyPort.findById(storyId)).thenReturn(Optional.of(story));

    // Act
    Story result = service.getStory(storyId, requesterId, "bearer-token");

    // Assert
    assertEquals(story, result);
    verify(storyPort).findById(storyId);
    // Community service should not be called when requester is author
    verifyNoInteractions(communityServicePort);
  }

  // =========================================================================
  // getStory - story not found tests
  // =========================================================================

  @Test
  @DisplayName("getStory should throw when story not found")
  void testGetStoryNotFound() {
    // Arrange
    UUID storyId = UUID.randomUUID();
    UUID requesterId = UUID.randomUUID();

    when(storyPort.findById(storyId)).thenReturn(Optional.empty());

    // Act & Assert
    StoryAccessDeniedException ex =
        assertThrows(
            StoryAccessDeniedException.class,
            () -> service.getStory(storyId, requesterId, "bearer-token"));
    assertEquals("Story not found", ex.getMessage());
    verify(storyPort).findById(storyId);
    verifyNoInteractions(communityServicePort);
  }

  // =========================================================================
  // getStory - PUBLIC shared level tests
  // =========================================================================

  @Test
  @DisplayName("getStory PUBLIC story is accessible to any relationship type")
  void testGetStoryPublicShareAllowsAny() {
    // Arrange
    UUID storyId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    UUID requesterId = UUID.randomUUID();

    Story story =
        new Story(storyId, authorId, "public story", SharedWith.PUBLIC, OffsetDateTime.now());
    when(storyPort.findById(storyId)).thenReturn(Optional.of(story));
    when(communityServicePort.getRelationship(authorId, "bearer-token"))
        .thenReturn(RelationshipType.COMMUNITY);

    // Act
    Story result = service.getStory(storyId, requesterId, "bearer-token");

    // Assert
    assertEquals(story, result);
    verify(communityServicePort).getRelationship(authorId, "bearer-token");
  }

  // =========================================================================
  // getStory - COMMUNITY shared level tests
  // =========================================================================

  @Test
  @DisplayName("getStory COMMUNITY story is accessible to any relationship type")
  void testGetStoryCommunityShareAllowsAny() {
    // Arrange
    UUID storyId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    UUID requesterId = UUID.randomUUID();

    Story story =
        new Story(storyId, authorId, "community story", SharedWith.COMMUNITY, OffsetDateTime.now());
    when(storyPort.findById(storyId)).thenReturn(Optional.of(story));
    when(communityServicePort.getRelationship(authorId, "bearer-token"))
        .thenReturn(RelationshipType.TRUSTED);

    // Act
    Story result = service.getStory(storyId, requesterId, "bearer-token");

    // Assert
    assertEquals(story, result);
  }

  // =========================================================================
  // getStory - CONNECTED shared level tests
  // =========================================================================

  @Test
  @DisplayName("getStory CONNECTED story is accessible to TRUSTED relationship")
  void testGetStoryConnectedAllowsTrusted() {
    // Arrange
    UUID storyId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    UUID requesterId = UUID.randomUUID();

    Story story =
        new Story(storyId, authorId, "connected story", SharedWith.CONNECTED, OffsetDateTime.now());
    when(storyPort.findById(storyId)).thenReturn(Optional.of(story));
    when(communityServicePort.getRelationship(authorId, "bearer-token"))
        .thenReturn(RelationshipType.TRUSTED);

    // Act
    Story result = service.getStory(storyId, requesterId, "bearer-token");

    // Assert
    assertEquals(story, result);
  }

  @Test
  @DisplayName("getStory CONNECTED story is accessible to CONNECTED relationship")
  void testGetStoryConnectedAllowsConnected() {
    // Arrange
    UUID storyId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    UUID requesterId = UUID.randomUUID();

    Story story =
        new Story(storyId, authorId, "connected story", SharedWith.CONNECTED, OffsetDateTime.now());
    when(storyPort.findById(storyId)).thenReturn(Optional.of(story));
    when(communityServicePort.getRelationship(authorId, "bearer-token"))
        .thenReturn(RelationshipType.CONNECTED);

    // Act
    Story result = service.getStory(storyId, requesterId, "bearer-token");

    // Assert
    assertEquals(story, result);
  }

  @Test
  @DisplayName("getStory CONNECTED story is denied to COMMUNITY relationship")
  void testGetStoryConnectedDeniesCommity() {
    // Arrange
    UUID storyId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    UUID requesterId = UUID.randomUUID();

    Story story =
        new Story(storyId, authorId, "connected story", SharedWith.CONNECTED, OffsetDateTime.now());
    when(storyPort.findById(storyId)).thenReturn(Optional.of(story));
    when(communityServicePort.getRelationship(authorId, "bearer-token"))
        .thenReturn(RelationshipType.COMMUNITY);

    // Act & Assert
    StoryAccessDeniedException ex =
        assertThrows(
            StoryAccessDeniedException.class,
            () -> service.getStory(storyId, requesterId, "bearer-token"));
    assertEquals("Story is not accessible to you", ex.getMessage());
  }

  // =========================================================================
  // getStory - TRUSTED shared level tests
  // =========================================================================

  @Test
  @DisplayName("getStory TRUSTED story is accessible to TRUSTED relationship only")
  void testGetStoryTrustedAllowsTrustedOnly() {
    // Arrange
    UUID storyId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    UUID requesterId = UUID.randomUUID();

    Story story =
        new Story(storyId, authorId, "trusted story", SharedWith.TRUSTED, OffsetDateTime.now());
    when(storyPort.findById(storyId)).thenReturn(Optional.of(story));
    when(communityServicePort.getRelationship(authorId, "bearer-token"))
        .thenReturn(RelationshipType.TRUSTED);

    // Act
    Story result = service.getStory(storyId, requesterId, "bearer-token");

    // Assert
    assertEquals(story, result);
  }

  @Test
  @DisplayName("getStory TRUSTED story is denied to CONNECTED relationship")
  void testGetStoryTrustedDeniesConnected() {
    // Arrange
    UUID storyId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    UUID requesterId = UUID.randomUUID();

    Story story =
        new Story(storyId, authorId, "trusted story", SharedWith.TRUSTED, OffsetDateTime.now());
    when(storyPort.findById(storyId)).thenReturn(Optional.of(story));
    when(communityServicePort.getRelationship(authorId, "bearer-token"))
        .thenReturn(RelationshipType.CONNECTED);

    // Act & Assert
    StoryAccessDeniedException ex =
        assertThrows(
            StoryAccessDeniedException.class,
            () -> service.getStory(storyId, requesterId, "bearer-token"));
    assertEquals("Story is not accessible to you", ex.getMessage());
  }

  @Test
  @DisplayName("getStory TRUSTED story is denied to COMMUNITY relationship")
  void testGetStoryTrustedDeniesCommunity() {
    // Arrange
    UUID storyId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    UUID requesterId = UUID.randomUUID();

    Story story =
        new Story(storyId, authorId, "trusted story", SharedWith.TRUSTED, OffsetDateTime.now());
    when(storyPort.findById(storyId)).thenReturn(Optional.of(story));
    when(communityServicePort.getRelationship(authorId, "bearer-token"))
        .thenReturn(RelationshipType.COMMUNITY);

    // Act & Assert
    assertThrows(
        StoryAccessDeniedException.class,
        () -> service.getStory(storyId, requesterId, "bearer-token"));
  }

  // =========================================================================
  // getStoriesNearMe tests
  // =========================================================================

  @Test
  @DisplayName("getStoriesNearMe should retrieve circles and query stories with pagination")
  void testGetStoriesNearMeRetrievesStoriesFromCircles() {
    // Arrange
    UUID trustedMemberId1 = UUID.randomUUID();
    UUID trustedMemberId2 = UUID.randomUUID();
    UUID connectedMemberId1 = UUID.randomUUID();
    UUID connectedMemberId2 = UUID.randomUUID();

    List<UUID> trustedMembers = List.of(trustedMemberId1, trustedMemberId2);
    List<UUID> connectedMembers = List.of(connectedMemberId1, connectedMemberId2);

    MyCircles circles = new MyCircles(trustedMembers, connectedMembers);

    Story story1 =
        new Story(
            UUID.randomUUID(),
            trustedMemberId1,
            "story 1",
            SharedWith.TRUSTED,
            OffsetDateTime.now());
    Story story2 =
        new Story(
            UUID.randomUUID(),
            connectedMemberId1,
            "story 2",
            SharedWith.CONNECTED,
            OffsetDateTime.now());
    List<Story> stories = List.of(story1, story2);

    when(communityServicePort.getMyCircles("bearer-token")).thenReturn(circles);
    when(storyPort.findStoriesByAuthorIds(trustedMembers, connectedMembers, 0, 10))
        .thenReturn(stories);

    // Act
    List<Story> result = service.getStoriesNearMe("bearer-token", 0, 10);

    // Assert
    assertEquals(2, result.size());
    assertEquals(story1, result.get(0));
    assertEquals(story2, result.get(1));

    verify(communityServicePort).getMyCircles("bearer-token");
    verify(storyPort).findStoriesByAuthorIds(trustedMembers, connectedMembers, 0, 10);
  }

  @Test
  @DisplayName("getStoriesNearMe should handle pagination with offset and count")
  void testGetStoriesNearMeHandlesPagination() {
    // Arrange
    UUID trustedMemberId = UUID.randomUUID();
    UUID connectedMemberId = UUID.randomUUID();

    List<UUID> trustedMembers = List.of(trustedMemberId);
    List<UUID> connectedMembers = List.of(connectedMemberId);

    MyCircles circles = new MyCircles(trustedMembers, connectedMembers);

    Story story =
        new Story(
            UUID.randomUUID(), trustedMemberId, "story", SharedWith.TRUSTED, OffsetDateTime.now());
    List<Story> stories = List.of(story);

    when(communityServicePort.getMyCircles("bearer-token")).thenReturn(circles);
    when(storyPort.findStoriesByAuthorIds(trustedMembers, connectedMembers, 10, 20))
        .thenReturn(stories);

    // Act
    List<Story> result = service.getStoriesNearMe("bearer-token", 10, 20);

    // Assert
    assertEquals(1, result.size());
    verify(storyPort).findStoriesByAuthorIds(trustedMembers, connectedMembers, 10, 20);
  }

  @Test
  @DisplayName("getStoriesNearMe should return empty list when no stories found")
  void testGetStoriesNearMeReturnsEmptyList() {
    // Arrange
    UUID trustedMemberId = UUID.randomUUID();
    UUID connectedMemberId = UUID.randomUUID();

    List<UUID> trustedMembers = List.of(trustedMemberId);
    List<UUID> connectedMembers = List.of(connectedMemberId);

    MyCircles circles = new MyCircles(trustedMembers, connectedMembers);

    when(communityServicePort.getMyCircles("bearer-token")).thenReturn(circles);
    when(storyPort.findStoriesByAuthorIds(trustedMembers, connectedMembers, 0, 10))
        .thenReturn(List.of());

    // Act
    List<Story> result = service.getStoriesNearMe("bearer-token", 0, 10);

    // Assert
    assertTrue(result.isEmpty());
    verify(communityServicePort).getMyCircles("bearer-token");
    verify(storyPort).findStoriesByAuthorIds(trustedMembers, connectedMembers, 0, 10);
  }
}
