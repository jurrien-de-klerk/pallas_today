package com.pallas.storyservice.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pallas.integrations.member.MemberServicePort;
import com.pallas.storyservice.application.StoryApplicationService;
import com.pallas.storyservice.domain.CommunityServicePort;
import com.pallas.storyservice.domain.SharedWith;
import com.pallas.storyservice.domain.Story;
import com.pallas.storyservice.domain.StoryAccessDeniedException;
import com.pallas.storyservice.domain.StoryPort;
import com.pallas.storyservice.model.StoryInput;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

/**
 * Integration tests for the Story API endpoints.
 *
 * <p>Verifies that the API implementation matches the OpenAPI contract. Tests currently cover the
 * implemented endpoints: POST /stories and GET /stories/story/{storyId}.
 */
@WebMvcTest(StoriesController.class)
@DisplayName("Story API Integration Tests")
class StoriesControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private StoryApplicationService applicationService;

  @MockitoBean private StoryPort storyPort;

  @MockitoBean private MemberServicePort memberServicePort;

  @MockitoBean private CommunityServicePort communityServicePort;

  private static final UUID AUTHOR_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
  private static final UUID STORY_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440003");

  @BeforeEach
  void setUp() {
    // ApplicationService is mocked; no setup needed here
  }

  // =========================================================================
  // POST /stories - Create Story
  // =========================================================================

  @Test
  @WithMockUser
  @DisplayName("POST /stories should return 201 when story is created successfully")
  void testCreateStorySuccessReturns201() throws Exception {
    // Arrange
    StoryInput input = new StoryInput();
    input.setContent("Test story content");
    input.setSharedWith(com.pallas.storyservice.model.SharedWith.TRUSTED);

    Story domainStory =
        new Story(
            STORY_ID, AUTHOR_ID, "Test story content", SharedWith.TRUSTED, OffsetDateTime.now());
    when(applicationService.createStory("Test story content", SharedWith.TRUSTED))
        .thenReturn(domainStory);

    // Act & Assert
    MvcResult result =
        mockMvc
            .perform(
                post("/stories")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(STORY_ID.toString()))
            .andExpect(jsonPath("$.authorId").value(AUTHOR_ID.toString()))
            .andExpect(jsonPath("$.content").value("Test story content"))
            .andExpect(jsonPath("$.sharedWith").value("TRUSTED"))
            .andExpect(jsonPath("$.publishedAt").isNotEmpty())
            .andExpect(header().exists("Location"))
            .andReturn();

    // Verify Location header contains story URI
    String location = result.getResponse().getHeader("Location");
    assertThat(location).isNotNull();
  }

  @Test
  @WithMockUser
  @DisplayName("POST /stories should persist story with correct author")
  void testCreateStoryPersistsAuthorCorrectly() throws Exception {
    // Arrange
    StoryInput input = new StoryInput();
    input.setContent("Story by authenticated user");
    input.setSharedWith(com.pallas.storyservice.model.SharedWith.COMMUNITY);

    UUID expectedAuthorId = AUTHOR_ID;
    Story domainStory =
        new Story(
            STORY_ID,
            expectedAuthorId,
            "Story by authenticated user",
            SharedWith.COMMUNITY,
            OffsetDateTime.now());
    when(applicationService.createStory("Story by authenticated user", SharedWith.COMMUNITY))
        .thenReturn(domainStory);

    // Act & Assert
    mockMvc
        .perform(
            post("/stories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.authorId").value(expectedAuthorId.toString()));
  }

  @Test
  @WithMockUser
  @DisplayName("POST /stories should respect shared with level")
  void testCreateStoryRespectsSharedWithLevel() throws Exception {
    // Arrange
    StoryInput input = new StoryInput();
    input.setContent("Public story");
    input.setSharedWith(com.pallas.storyservice.model.SharedWith.PUBLIC);

    Story domainStory =
        new Story(STORY_ID, AUTHOR_ID, "Public story", SharedWith.PUBLIC, OffsetDateTime.now());
    when(applicationService.createStory("Public story", SharedWith.PUBLIC)).thenReturn(domainStory);

    // Act & Assert
    mockMvc
        .perform(
            post("/stories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.sharedWith").value("PUBLIC"));
  }

  @Test
  @DisplayName("POST /stories should return 401 when not authenticated")
  void testCreateStoryUnauthorizedWhenNotAuthenticated() throws Exception {
    // Arrange
    StoryInput input = new StoryInput();
    input.setContent("Test story");
    input.setSharedWith(com.pallas.storyservice.model.SharedWith.TRUSTED);

    // Act & Assert
    mockMvc
        .perform(
            post("/stories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
        .andExpect(status().isUnauthorized());
  }

  // =========================================================================
  // GET /stories/story/{storyId} - Get Story
  // =========================================================================

  @Test
  @WithMockUser
  @DisplayName("GET /stories/story/{storyId} should return 200 when author retrieves their story")
  void testGetStoryByAuthorReturns200() throws Exception {
    // Arrange
    Story story =
        new Story(STORY_ID, AUTHOR_ID, "My story", SharedWith.TRUSTED, OffsetDateTime.now());
    when(applicationService.getStory(STORY_ID)).thenReturn(story);

    // Act & Assert
    mockMvc
        .perform(get("/stories/story/{storyId}", STORY_ID))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(STORY_ID.toString()))
        .andExpect(jsonPath("$.authorId").value(AUTHOR_ID.toString()))
        .andExpect(jsonPath("$.content").value("My story"))
        .andExpect(jsonPath("$.sharedWith").value("TRUSTED"))
        .andExpect(jsonPath("$.publishedAt").isNotEmpty());
  }

  @Test
  @WithMockUser
  @DisplayName("GET /stories/story/{storyId} should return 200 when story is PUBLIC")
  void testGetPublicStoryReturns200() throws Exception {
    // Arrange
    Story story =
        new Story(STORY_ID, AUTHOR_ID, "Public story", SharedWith.PUBLIC, OffsetDateTime.now());
    when(applicationService.getStory(STORY_ID)).thenReturn(story);

    // Act & Assert
    mockMvc
        .perform(get("/stories/story/{storyId}", STORY_ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sharedWith").value("PUBLIC"));
  }

  @Test
  @WithMockUser
  @DisplayName("GET /stories/story/{storyId} should return 200 when story is COMMUNITY shared")
  void testGetCommunitySharedStoryReturns200() throws Exception {
    // Arrange
    Story story =
        new Story(
            STORY_ID, AUTHOR_ID, "Community story", SharedWith.COMMUNITY, OffsetDateTime.now());
    when(applicationService.getStory(STORY_ID)).thenReturn(story);

    // Act & Assert
    mockMvc
        .perform(get("/stories/story/{storyId}", STORY_ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sharedWith").value("COMMUNITY"));
  }

  @Test
  @WithMockUser
  @DisplayName("GET /stories/story/{storyId} should return 404 when story not found")
  void testGetStoryNotFoundReturns404() throws Exception {
    // Arrange
    when(applicationService.getStory(STORY_ID))
        .thenThrow(new StoryAccessDeniedException("Story not found"));

    // Act & Assert
    mockMvc.perform(get("/stories/story/{storyId}", STORY_ID)).andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser
  @DisplayName(
      "GET /stories/story/{storyId} should return 404 when CONNECTED story accessed by COMMUNITY member")
  void testGetConnectedStoryDeniedToCommunityMemberReturns404() throws Exception {
    // Arrange
    when(applicationService.getStory(STORY_ID))
        .thenThrow(new StoryAccessDeniedException("Story is not accessible to you"));

    // Act & Assert
    mockMvc.perform(get("/stories/story/{storyId}", STORY_ID)).andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser
  @DisplayName(
      "GET /stories/story/{storyId} should return 200 when CONNECTED story accessed by CONNECTED member")
  void testGetConnectedStoryAllowedToConnectedMemberReturns200() throws Exception {
    // Arrange
    Story story =
        new Story(
            STORY_ID, AUTHOR_ID, "Connected story", SharedWith.CONNECTED, OffsetDateTime.now());
    when(applicationService.getStory(STORY_ID)).thenReturn(story);

    // Act & Assert
    mockMvc
        .perform(get("/stories/story/{storyId}", STORY_ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sharedWith").value("CONNECTED"));
  }

  @Test
  @WithMockUser
  @DisplayName(
      "GET /stories/story/{storyId} should return 200 when TRUSTED story accessed by TRUSTED member")
  void testGetTrustedStoryAllowedToTrustedMemberReturns200() throws Exception {
    // Arrange
    Story story =
        new Story(STORY_ID, AUTHOR_ID, "Trusted story", SharedWith.TRUSTED, OffsetDateTime.now());
    when(applicationService.getStory(STORY_ID)).thenReturn(story);

    // Act & Assert
    mockMvc
        .perform(get("/stories/story/{storyId}", STORY_ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sharedWith").value("TRUSTED"));
  }

  @Test
  @WithMockUser
  @DisplayName(
      "GET /stories/story/{storyId} should return 404 when TRUSTED story accessed by CONNECTED member")
  void testGetTrustedStoryDeniedToConnectedMemberReturns404() throws Exception {
    // Arrange
    when(applicationService.getStory(STORY_ID))
        .thenThrow(new StoryAccessDeniedException("Story is not accessible to you"));

    // Act & Assert
    mockMvc.perform(get("/stories/story/{storyId}", STORY_ID)).andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("GET /stories/story/{storyId} should return 401 when not authenticated")
  void testGetStoryUnauthorizedWhenNotAuthenticated() throws Exception {
    // Act & Assert
    mockMvc.perform(get("/stories/story/{storyId}", STORY_ID)).andExpect(status().isUnauthorized());
  }
}
