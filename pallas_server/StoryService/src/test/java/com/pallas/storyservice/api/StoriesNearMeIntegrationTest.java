package com.pallas.storyservice.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pallas.integrations.member.MemberServicePort;
import com.pallas.storyservice.data.SharedWithEntity;
import com.pallas.storyservice.data.StoryEntity;
import com.pallas.storyservice.data.StoryRepository;
import com.pallas.storyservice.domain.CommunityServicePort;
import com.pallas.storyservice.domain.MyCircles;
import com.pallas.storyservice.domain.RelationshipType;
import com.pallas.storyservice.model.StoryInput;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Full-stack integration tests for Story API with real database persistence.
 *
 * <p>Tests the entire application from HTTP request through API controller → application service →
 * domain logic → persistence adapter → actual database. This catches integration issues that
 * unit/mock tests miss, such as Hibernate entity mapping problems or database constraint
 * violations.
 *
 * <p>External services (Member Service, Community Service) are mocked to isolate the Story Service
 * stack.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Story Near Me API Integration Tests")
class StoriesNearMeIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private StoryRepository storyRepository;

  @MockitoBean private MemberServicePort memberServicePort;

  @MockitoBean private CommunityServicePort communityServicePort;

  private static final UUID AUTHENTICATED_MEMBER_ID =
      UUID.fromString("550e8400-e29b-41d4-a716-446655440099");

  @BeforeEach
  void setUp() {
    storyRepository.deleteAll();
    when(memberServicePort.resolveCurrentMemberId(anyString())).thenReturn(AUTHENTICATED_MEMBER_ID);
    when(communityServicePort.getRelationship(any(UUID.class), anyString()))
        .thenReturn(RelationshipType.TRUSTED);
    when(communityServicePort.getMyCircles(anyString()))
        .thenReturn(new MyCircles(List.of(AUTHENTICATED_MEMBER_ID), List.of()));
  }

  // =========================================================================
  // GET /stories/near-me - Stories from Circles (Full Stack)
  // =========================================================================

  @Test
  @DisplayName(
      "GET /stories/near-me should return stories with default pagination (offset=0, count=20)")
  void testGetStoriesNearMeWithDefaults() throws Exception {
    // Arrange - Create a story in authenticated user's circles
    StoryInput input = new StoryInput();
    input.setContent(List.of(Map.of("insert", "Story from circle")));
    input.setSharedWith(com.pallas.storyservice.model.SharedWith.TRUSTED);

    String createResponse =
        mockMvc
            .perform(
                post("/stories")
                    .with(jwt().jwt(jwt -> jwt.claim("sub", AUTHENTICATED_MEMBER_ID.toString())))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    String storyId = objectMapper.readTree(createResponse).get("id").asText();

    // Act & Assert - Retrieve from near-me without parameters
    mockMvc
        .perform(
            get("/stories/near-me")
                .with(jwt().jwt(jwt -> jwt.claim("sub", AUTHENTICATED_MEMBER_ID.toString()))))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.stories").isArray())
        .andExpect(jsonPath("$.stories[0].id").value(storyId))
        .andExpect(jsonPath("$.stories[0].content[0].insert").value("Story from circle"))
        .andExpect(jsonPath("$.stories[0].sharedWith").value("TRUSTED"));
  }

  @Test
  @DisplayName("GET /stories/near-me should respect offset and count pagination parameters")
  void testGetStoriesNearMeWithPagination() throws Exception {
    // Arrange - Create 3 stories
    for (int i = 1; i <= 3; i++) {
      StoryInput input = new StoryInput();
      input.setContent(List.of(Map.of("insert", "Story " + i)));
      input.setSharedWith(com.pallas.storyservice.model.SharedWith.TRUSTED);

      mockMvc
          .perform(
              post("/stories")
                  .with(jwt().jwt(jwt -> jwt.claim("sub", AUTHENTICATED_MEMBER_ID.toString())))
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(input)))
          .andExpect(status().isCreated());
    }

    // Act & Assert - Get second story (offset=1, count=1)
    mockMvc
        .perform(
            get("/stories/near-me?offset=1&count=1")
                .with(jwt().jwt(jwt -> jwt.claim("sub", AUTHENTICATED_MEMBER_ID.toString()))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.stories.length()").value(1))
        .andExpect(jsonPath("$.stories[0].content[0].insert").value("Story 2"));

    // Act & Assert - Get first 2 stories (offset=0, count=2)
    mockMvc
        .perform(
            get("/stories/near-me?offset=0&count=2")
                .with(jwt().jwt(jwt -> jwt.claim("sub", AUTHENTICATED_MEMBER_ID.toString()))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.stories.length()").value(2));
  }

  @Test
  @DisplayName("GET /stories/near-me should return empty array when no stories exist")
  void testGetStoriesNearMeEmptyWhenNoStories() throws Exception {
    // Act & Assert - No stories created, should return empty array
    mockMvc
        .perform(
            get("/stories/near-me")
                .with(jwt().jwt(jwt -> jwt.claim("sub", AUTHENTICATED_MEMBER_ID.toString()))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.stories").isArray())
        .andExpect(jsonPath("$.stories.length()").value(0));
  }

  @Test
  @DisplayName("GET /stories/near-me should return 401 when not authenticated")
  void testGetStoriesNearMeUnauthorizedWhenNotAuthenticated() throws Exception {
    // Act & Assert
    mockMvc.perform(get("/stories/near-me")).andExpect(status().isUnauthorized());
  }

  @Test
  @DisplayName("GET /stories/near-me should order stories by publishedAt descending (newest first)")
  void testGetStoriesNearMeOrderByNewest() throws Exception {
    // Arrange - Create stories with slight delays to ensure different timestamps
    List<String> storyIds = new java.util.ArrayList<>();
    for (int i = 1; i <= 3; i++) {
      StoryInput input = new StoryInput();
      input.setContent(List.of(Map.of("insert", "Story " + i)));
      input.setSharedWith(com.pallas.storyservice.model.SharedWith.TRUSTED);

      String response =
          mockMvc
              .perform(
                  post("/stories")
                      .with(jwt().jwt(jwt -> jwt.claim("sub", AUTHENTICATED_MEMBER_ID.toString())))
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsString(input)))
              .andExpect(status().isCreated())
              .andReturn()
              .getResponse()
              .getContentAsString();

      storyIds.add(objectMapper.readTree(response).get("id").asText());
      Thread.sleep(10); // Small delay to ensure different timestamps
    }

    // Act & Assert - Most recent story should be first
    mockMvc
        .perform(
            get("/stories/near-me")
                .with(jwt().jwt(jwt -> jwt.claim("sub", AUTHENTICATED_MEMBER_ID.toString()))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.stories[0].id").value(storyIds.get(2))) // Most recent
        .andExpect(jsonPath("$.stories[1].id").value(storyIds.get(1)))
        .andExpect(jsonPath("$.stories[2].id").value(storyIds.get(0))); // Oldest
  }

  @Test
  @DisplayName(
      "GET /stories/near-me should not include stories from connected members with TRUSTED-only sharing")
  void testGetStoriesNearMeRespectsTrustedSharingForConnectedMembers() throws Exception {
    // Arrange - Set up circles with both trusted and connected members
    UUID connectedMemberId = UUID.fromString("550e8400-e29b-41d4-a716-446655440088");
    when(communityServicePort.getMyCircles(anyString()))
        .thenReturn(
            new MyCircles(
                List.of(AUTHENTICATED_MEMBER_ID),
                List.of(connectedMemberId))); // Authenticated member in trusted, connected member
    // in connected

    OffsetDateTime now = OffsetDateTime.now();
    String trustedContent =
        objectMapper.writeValueAsString(List.of(Map.of("insert", "My trusted story")));
    String connectedTrustedContent =
        objectMapper.writeValueAsString(
            List.of(Map.of("insert", "Connected member trusted story")));
    String connectedSharedContent =
        objectMapper.writeValueAsString(List.of(Map.of("insert", "Connected member shared story")));

    // Create stories directly in repository with correct author IDs to bypass API mock issues
    UUID trustedStoryId = UUID.randomUUID();
    StoryEntity myTrustedStory = new StoryEntity();
    myTrustedStory.setId(trustedStoryId);
    myTrustedStory.setAuthorId(AUTHENTICATED_MEMBER_ID);
    myTrustedStory.setContent(trustedContent);
    myTrustedStory.setSharedWith(SharedWithEntity.TRUSTED);
    myTrustedStory.setPublishedAt(now);
    storyRepository.save(myTrustedStory);

    // Create story by connected member with TRUSTED sharing (should NOT be visible)
    UUID connectedTrustedStoryId = UUID.randomUUID();
    StoryEntity connectedTrustedStory = new StoryEntity();
    connectedTrustedStory.setId(connectedTrustedStoryId);
    connectedTrustedStory.setAuthorId(connectedMemberId);
    connectedTrustedStory.setContent(connectedTrustedContent);
    connectedTrustedStory.setSharedWith(SharedWithEntity.TRUSTED);
    connectedTrustedStory.setPublishedAt(now.plusSeconds(1)); // Slightly later
    storyRepository.save(connectedTrustedStory);

    // Create story by connected member with CONNECTED sharing (should be visible)
    UUID connectedSharedStoryId = UUID.randomUUID();
    StoryEntity connectedSharedStory = new StoryEntity();
    connectedSharedStory.setId(connectedSharedStoryId);
    connectedSharedStory.setAuthorId(connectedMemberId);
    connectedSharedStory.setContent(connectedSharedContent);
    connectedSharedStory.setSharedWith(SharedWithEntity.CONNECTED);
    connectedSharedStory.setPublishedAt(now.plusSeconds(2)); // Most recent
    storyRepository.save(connectedSharedStory);

    // Act & Assert - Fetch near-me and verify only visible stories are returned
    mockMvc
        .perform(
            get("/stories/near-me")
                .with(jwt().jwt(jwt -> jwt.claim("sub", AUTHENTICATED_MEMBER_ID.toString()))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.stories").isArray())
        .andExpect(jsonPath("$.stories.length()").value(2)) // Only trusted and connected-shared,
        // not connected-trusted
        .andExpect(
            jsonPath("$.stories[0].id").value(connectedSharedStoryId.toString())) // Most recent
        .andExpect(jsonPath("$.stories[1].id").value(trustedStoryId.toString())); // Less recent
  }

  @Test
  @DisplayName(
      "GET /stories/near-me should include stories from trusted members shared with CONNECTED level")
  void testGetStoriesNearMeIncludesConnectedSharingForTrustedMembers() throws Exception {
    // Arrange - Authenticated member in trusted circle, author shares with CONNECTED
    UUID trustedMemberId = UUID.fromString("550e8400-e29b-41d4-a716-446655440077");
    when(communityServicePort.getMyCircles(anyString()))
        .thenReturn(new MyCircles(List.of(trustedMemberId, AUTHENTICATED_MEMBER_ID), List.of()));

    OffsetDateTime now = OffsetDateTime.now();
    UUID storyId = UUID.randomUUID();
    StoryEntity story = new StoryEntity();
    story.setId(storyId);
    story.setAuthorId(trustedMemberId);
    story.setContent(
        objectMapper.writeValueAsString(List.of(Map.of("insert", "Shared with connected"))));
    story.setSharedWith(SharedWithEntity.CONNECTED);
    story.setPublishedAt(now);
    storyRepository.save(story);

    // Act & Assert
    mockMvc
        .perform(
            get("/stories/near-me")
                .with(jwt().jwt(jwt -> jwt.claim("sub", AUTHENTICATED_MEMBER_ID.toString()))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.stories.length()").value(1))
        .andExpect(jsonPath("$.stories[0].id").value(storyId.toString()))
        .andExpect(jsonPath("$.stories[0].sharedWith").value("CONNECTED"));
  }

  @Test
  @DisplayName(
      "GET /stories/near-me should exclude stories from non-circle members with TRUSTED sharing")
  void testGetStoriesNearMeExcludesTrustedSharingFromNonCircleMembers() throws Exception {
    // Arrange - No relationship with author (not in circles)
    UUID strangerMemberId = UUID.fromString("550e8400-e29b-41d4-a716-446655440055");
    when(communityServicePort.getMyCircles(anyString()))
        .thenReturn(new MyCircles(List.of(AUTHENTICATED_MEMBER_ID), List.of()));

    OffsetDateTime now = OffsetDateTime.now();
    UUID storyId = UUID.randomUUID();
    StoryEntity story = new StoryEntity();
    story.setId(storyId);
    story.setAuthorId(strangerMemberId);
    story.setContent(objectMapper.writeValueAsString(List.of(Map.of("insert", "Trusted only"))));
    story.setSharedWith(SharedWithEntity.TRUSTED);
    story.setPublishedAt(now);
    storyRepository.save(story);

    // Act & Assert
    mockMvc
        .perform(
            get("/stories/near-me")
                .with(jwt().jwt(jwt -> jwt.claim("sub", AUTHENTICATED_MEMBER_ID.toString()))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.stories.length()").value(0));
  }

  @Test
  @DisplayName(
      "GET /stories/near-me should exclude stories from non-circle members with CONNECTED sharing")
  void testGetStoriesNearMeExcludesConnectedSharingFromNonCircleMembers() throws Exception {
    // Arrange - No relationship with author (not in circles)
    UUID strangerMemberId = UUID.fromString("550e8400-e29b-41d4-a716-446655440044");
    when(communityServicePort.getMyCircles(anyString()))
        .thenReturn(new MyCircles(List.of(AUTHENTICATED_MEMBER_ID), List.of()));

    OffsetDateTime now = OffsetDateTime.now();
    UUID storyId = UUID.randomUUID();
    StoryEntity story = new StoryEntity();
    story.setId(storyId);
    story.setAuthorId(strangerMemberId);
    story.setContent(objectMapper.writeValueAsString(List.of(Map.of("insert", "Connected only"))));
    story.setSharedWith(SharedWithEntity.CONNECTED);
    story.setPublishedAt(now);
    storyRepository.save(story);

    // Act & Assert
    mockMvc
        .perform(
            get("/stories/near-me")
                .with(jwt().jwt(jwt -> jwt.claim("sub", AUTHENTICATED_MEMBER_ID.toString()))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.stories.length()").value(0));
  }

  @Test
  @DisplayName(
      "GET /stories/near-me should include all non-TRUSTED sharing levels for trusted circle members")
  void testGetStoriesNearMeIncludesAllSharingLevelsForTrustedMembers() throws Exception {
    // Arrange - Trusted member shares at all levels: TRUSTED, CONNECTED, COMMUNITY, PUBLIC
    UUID trustedMemberId = UUID.fromString("550e8400-e29b-41d4-a716-446655440033");
    when(communityServicePort.getMyCircles(anyString()))
        .thenReturn(new MyCircles(List.of(trustedMemberId, AUTHENTICATED_MEMBER_ID), List.of()));

    OffsetDateTime now = OffsetDateTime.now();
    com.pallas.storyservice.model.SharedWith[] levels = {
      com.pallas.storyservice.model.SharedWith.TRUSTED,
      com.pallas.storyservice.model.SharedWith.CONNECTED,
      com.pallas.storyservice.model.SharedWith.COMMUNITY,
      com.pallas.storyservice.model.SharedWith.PUBLIC
    };

    java.util.List<UUID> storyIds = new java.util.ArrayList<>();
    for (int i = 0; i < levels.length; i++) {
      UUID storyId = UUID.randomUUID();
      StoryEntity story = new StoryEntity();
      story.setId(storyId);
      story.setAuthorId(trustedMemberId);
      story.setContent(
          objectMapper.writeValueAsString(List.of(Map.of("insert", "Story with " + levels[i]))));
      story.setSharedWith(SharedWithEntity.valueOf(levels[i].toString()));
      story.setPublishedAt(now.plusSeconds(i)); // Different timestamps
      storyRepository.save(story);
      storyIds.add(storyId);
    }

    // Act & Assert - All stories should be visible, ordered by most recent first
    mockMvc
        .perform(
            get("/stories/near-me")
                .with(jwt().jwt(jwt -> jwt.claim("sub", AUTHENTICATED_MEMBER_ID.toString()))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.stories.length()").value(4))
        // Most recent (PUBLIC) first
        .andExpect(jsonPath("$.stories[0].id").value(storyIds.get(3).toString()))
        .andExpect(jsonPath("$.stories[0].sharedWith").value("PUBLIC"))
        // Then COMMUNITY
        .andExpect(jsonPath("$.stories[1].id").value(storyIds.get(2).toString()))
        .andExpect(jsonPath("$.stories[1].sharedWith").value("COMMUNITY"))
        // Then CONNECTED
        .andExpect(jsonPath("$.stories[2].id").value(storyIds.get(1).toString()))
        .andExpect(jsonPath("$.stories[2].sharedWith").value("CONNECTED"))
        // Oldest (TRUSTED)
        .andExpect(jsonPath("$.stories[3].id").value(storyIds.get(0).toString()))
        .andExpect(jsonPath("$.stories[3].sharedWith").value("TRUSTED"));
  }

  @Test
  @DisplayName(
      "GET /stories/near-me should include CONNECTED and PUBLIC for connected circle members, exclude TRUSTED")
  void testGetStoriesNearMePartialVisibilityForConnectedMembers() throws Exception {
    // Arrange - Connected member shares at all levels: TRUSTED, CONNECTED, COMMUNITY, PUBLIC
    UUID connectedMemberId = UUID.fromString("550e8400-e29b-41d4-a716-446655440022");
    when(communityServicePort.getMyCircles(anyString()))
        .thenReturn(new MyCircles(List.of(AUTHENTICATED_MEMBER_ID), List.of(connectedMemberId)));

    OffsetDateTime now = OffsetDateTime.now();
    UUID trustedStoryId = UUID.randomUUID();
    UUID connectedStoryId = UUID.randomUUID();
    UUID communityStoryId = UUID.randomUUID();
    UUID publicStoryId = UUID.randomUUID();

    StoryEntity trustedStory = new StoryEntity();
    trustedStory.setId(trustedStoryId);
    trustedStory.setAuthorId(connectedMemberId);
    trustedStory.setContent(
        objectMapper.writeValueAsString(List.of(Map.of("insert", "Trusted only"))));
    trustedStory.setSharedWith(SharedWithEntity.TRUSTED);
    trustedStory.setPublishedAt(now);
    storyRepository.save(trustedStory);

    StoryEntity connectedStory = new StoryEntity();
    connectedStory.setId(connectedStoryId);
    connectedStory.setAuthorId(connectedMemberId);
    connectedStory.setContent(
        objectMapper.writeValueAsString(List.of(Map.of("insert", "Connected"))));
    connectedStory.setSharedWith(SharedWithEntity.CONNECTED);
    connectedStory.setPublishedAt(now.plusSeconds(1));
    storyRepository.save(connectedStory);

    StoryEntity communityStory = new StoryEntity();
    communityStory.setId(communityStoryId);
    communityStory.setAuthorId(connectedMemberId);
    communityStory.setContent(
        objectMapper.writeValueAsString(List.of(Map.of("insert", "Community"))));
    communityStory.setSharedWith(SharedWithEntity.COMMUNITY);
    communityStory.setPublishedAt(now.plusSeconds(2));
    storyRepository.save(communityStory);

    StoryEntity publicStory = new StoryEntity();
    publicStory.setId(publicStoryId);
    publicStory.setAuthorId(connectedMemberId);
    publicStory.setContent(objectMapper.writeValueAsString(List.of(Map.of("insert", "Public"))));
    publicStory.setSharedWith(SharedWithEntity.PUBLIC);
    publicStory.setPublishedAt(now.plusSeconds(3));
    storyRepository.save(publicStory);

    // Act & Assert - Only CONNECTED, COMMUNITY, and PUBLIC visible, not TRUSTED
    mockMvc
        .perform(
            get("/stories/near-me")
                .with(jwt().jwt(jwt -> jwt.claim("sub", AUTHENTICATED_MEMBER_ID.toString()))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.stories.length()").value(3))
        // Most recent (PUBLIC) first
        .andExpect(jsonPath("$.stories[0].id").value(publicStoryId.toString()))
        // Then COMMUNITY
        .andExpect(jsonPath("$.stories[1].id").value(communityStoryId.toString()))
        // Then CONNECTED
        .andExpect(jsonPath("$.stories[2].id").value(connectedStoryId.toString()));
    // TRUSTED story should NOT be in results
  }

  @Test
  @DisplayName("GET /stories/near-me should return empty array when both circles are empty")
  void testGetStoriesNearMeEmptyWhenBothCirclesEmpty() throws Exception {
    // Arrange - No members in either circle (new user with no relationships)
    when(communityServicePort.getMyCircles(anyString()))
        .thenReturn(new MyCircles(List.of(), List.of()));

    // Create a story by an unrelated member
    UUID strangerMemberId = UUID.fromString("550e8400-e29b-41d4-a716-446655440011");
    OffsetDateTime now = OffsetDateTime.now();
    UUID storyId = UUID.randomUUID();
    StoryEntity story = new StoryEntity();
    story.setId(storyId);
    story.setAuthorId(strangerMemberId);
    story.setContent(objectMapper.writeValueAsString(List.of(Map.of("insert", "Public story"))));
    story.setSharedWith(SharedWithEntity.PUBLIC);
    story.setPublishedAt(now);
    storyRepository.save(story);

    // Act & Assert - Should return empty list (no members in circles, so no visible stories)
    mockMvc
        .perform(
            get("/stories/near-me")
                .with(jwt().jwt(jwt -> jwt.claim("sub", AUTHENTICATED_MEMBER_ID.toString()))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.stories").isArray())
        .andExpect(jsonPath("$.stories.length()").value(0));
  }

  @Test
  @DisplayName("GET /stories/near-me should return stories when only trusted circle has members")
  void testGetStoriesNearMeWithOnlyTrustedCirclePopulated() throws Exception {
    // Arrange - Only trusted circle has members, connected circle is empty
    UUID trustedMemberId = UUID.fromString("550e8400-e29b-41d4-a716-446655440012");
    when(communityServicePort.getMyCircles(anyString()))
        .thenReturn(new MyCircles(List.of(trustedMemberId, AUTHENTICATED_MEMBER_ID), List.of()));

    OffsetDateTime now = OffsetDateTime.now();

    // Create story by trusted member with all sharing levels
    java.util.List<UUID> storyIds = new java.util.ArrayList<>();
    com.pallas.storyservice.model.SharedWith[] levels = {
      com.pallas.storyservice.model.SharedWith.TRUSTED,
      com.pallas.storyservice.model.SharedWith.CONNECTED,
      com.pallas.storyservice.model.SharedWith.PUBLIC
    };

    for (int i = 0; i < levels.length; i++) {
      UUID storyId = UUID.randomUUID();
      StoryEntity story = new StoryEntity();
      story.setId(storyId);
      story.setAuthorId(trustedMemberId);
      story.setContent(
          objectMapper.writeValueAsString(List.of(Map.of("insert", "Story " + levels[i]))));
      story.setSharedWith(SharedWithEntity.valueOf(levels[i].toString()));
      story.setPublishedAt(now.plusSeconds(i));
      storyRepository.save(story);
      storyIds.add(storyId);
    }

    // Act & Assert - All trusted member stories should be visible
    mockMvc
        .perform(
            get("/stories/near-me")
                .with(jwt().jwt(jwt -> jwt.claim("sub", AUTHENTICATED_MEMBER_ID.toString()))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.stories.length()").value(3))
        // Most recent (PUBLIC) first
        .andExpect(jsonPath("$.stories[0].id").value(storyIds.get(2).toString()))
        .andExpect(jsonPath("$.stories[0].sharedWith").value("PUBLIC"))
        // Then CONNECTED
        .andExpect(jsonPath("$.stories[1].id").value(storyIds.get(1).toString()))
        .andExpect(jsonPath("$.stories[1].sharedWith").value("CONNECTED"))
        // Oldest (TRUSTED)
        .andExpect(jsonPath("$.stories[2].id").value(storyIds.get(0).toString()))
        .andExpect(jsonPath("$.stories[2].sharedWith").value("TRUSTED"));
  }

  @Test
  @DisplayName(
      "GET /stories/near-me should return filtered stories when only connected circle has members")
  void testGetStoriesNearMeWithOnlyConnectedCirclePopulated() throws Exception {
    // Arrange - Only connected circle has members, trusted circle is empty
    UUID connectedMemberId = UUID.fromString("550e8400-e29b-41d4-a716-446655440013");
    when(communityServicePort.getMyCircles(anyString()))
        .thenReturn(new MyCircles(List.of(AUTHENTICATED_MEMBER_ID), List.of(connectedMemberId)));

    OffsetDateTime now = OffsetDateTime.now();

    // Create stories at all sharing levels by connected member
    UUID trustedStoryId = UUID.randomUUID();
    StoryEntity trustedStory = new StoryEntity();
    trustedStory.setId(trustedStoryId);
    trustedStory.setAuthorId(connectedMemberId);
    trustedStory.setContent(objectMapper.writeValueAsString(List.of(Map.of("insert", "TRUSTED"))));
    trustedStory.setSharedWith(SharedWithEntity.TRUSTED);
    trustedStory.setPublishedAt(now);
    storyRepository.save(trustedStory);

    UUID connectedStoryId = UUID.randomUUID();
    StoryEntity connectedStory = new StoryEntity();
    connectedStory.setId(connectedStoryId);
    connectedStory.setAuthorId(connectedMemberId);
    connectedStory.setContent(
        objectMapper.writeValueAsString(List.of(Map.of("insert", "CONNECTED"))));
    connectedStory.setSharedWith(SharedWithEntity.CONNECTED);
    connectedStory.setPublishedAt(now.plusSeconds(1));
    storyRepository.save(connectedStory);

    UUID communityStoryId = UUID.randomUUID();
    StoryEntity communityStory = new StoryEntity();
    communityStory.setId(communityStoryId);
    communityStory.setAuthorId(connectedMemberId);
    communityStory.setContent(
        objectMapper.writeValueAsString(List.of(Map.of("insert", "COMMUNITY"))));
    communityStory.setSharedWith(SharedWithEntity.COMMUNITY);
    communityStory.setPublishedAt(now.plusSeconds(2));
    storyRepository.save(communityStory);

    UUID publicStoryId = UUID.randomUUID();
    StoryEntity publicStory = new StoryEntity();
    publicStory.setId(publicStoryId);
    publicStory.setAuthorId(connectedMemberId);
    publicStory.setContent(objectMapper.writeValueAsString(List.of(Map.of("insert", "PUBLIC"))));
    publicStory.setSharedWith(SharedWithEntity.PUBLIC);
    publicStory.setPublishedAt(now.plusSeconds(3));
    storyRepository.save(publicStory);

    // Act & Assert - Only CONNECTED and above should be visible (TRUSTED excluded)
    mockMvc
        .perform(
            get("/stories/near-me")
                .with(jwt().jwt(jwt -> jwt.claim("sub", AUTHENTICATED_MEMBER_ID.toString()))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.stories.length()").value(3))
        // Most recent (PUBLIC) first
        .andExpect(jsonPath("$.stories[0].id").value(publicStoryId.toString()))
        .andExpect(jsonPath("$.stories[0].sharedWith").value("PUBLIC"))
        // Then COMMUNITY
        .andExpect(jsonPath("$.stories[1].id").value(communityStoryId.toString()))
        .andExpect(jsonPath("$.stories[1].sharedWith").value("COMMUNITY"))
        // Then CONNECTED
        .andExpect(jsonPath("$.stories[2].id").value(connectedStoryId.toString()))
        .andExpect(jsonPath("$.stories[2].sharedWith").value("CONNECTED"));
    // TRUSTED story should NOT be visible
  }
}
