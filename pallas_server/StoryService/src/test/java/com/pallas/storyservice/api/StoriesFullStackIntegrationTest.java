package com.pallas.storyservice.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pallas.integrations.member.MemberServicePort;
import com.pallas.storyservice.data.StoryRepository;
import com.pallas.storyservice.domain.CommunityServicePort;
import com.pallas.storyservice.domain.RelationshipType;
import com.pallas.storyservice.model.StoryInput;
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
import org.springframework.test.web.servlet.MvcResult;

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
@DisplayName("Story API Full-Stack Integration Tests")
class StoriesFullStackIntegrationTest {

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
  }

  // =========================================================================
  // POST /stories - Create Story (Full Stack)
  // =========================================================================

  @Test
  @DisplayName("POST /stories should create and persist story with correct values in database")
  void testCreateStoryPersistsToDatabase() throws Exception {
    // Arrange
    StoryInput input = new StoryInput();
    input.setContent(
        java.util.List.of(
            java.util.Map.of("insert", "Test story"), java.util.Map.of("insert", " content")));
    input.setSharedWith(com.pallas.storyservice.model.SharedWith.TRUSTED);

    // Act - Create story via API
    MvcResult result =
        mockMvc
            .perform(
                post("/stories")
                    .with(jwt().jwt(jwt -> jwt.claim("sub", AUTHENTICATED_MEMBER_ID.toString())))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.authorId").value(AUTHENTICATED_MEMBER_ID.toString()))
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content[0].insert").value("Test story"))
            .andExpect(jsonPath("$.sharedWith").value("TRUSTED"))
            .andExpect(jsonPath("$.publishedAt").isNotEmpty())
            .andExpect(header().exists("Location"))
            .andReturn();

    // Extract story ID from response
    String responseBody = result.getResponse().getContentAsString();
    String storyId = objectMapper.readTree(responseBody).get("id").asText();

    // Assert - Verify story was actually persisted to database
    assertThat(storyRepository.findById(UUID.fromString(storyId)))
        .isPresent()
        .get()
        .satisfies(
            entity -> {
              assertThat(entity.getAuthorId()).isEqualTo(AUTHENTICATED_MEMBER_ID);
              assertThat(entity.getContent())
                  .contains("Test story")
                  .contains("content"); // Serialized JSON
              assertThat(entity.getSharedWith().toString()).isEqualTo("TRUSTED");
              assertThat(entity.getPublishedAt()).isNotNull();
            });
  }

  @Test
  @DisplayName("POST /stories should generate unique ID for each story")
  void testCreateStoryGeneratesUniqueIds() throws Exception {
    // Arrange
    StoryInput input1 = new StoryInput();
    input1.setContent(java.util.List.of(java.util.Map.of("insert", "Story 1")));
    input1.setSharedWith(com.pallas.storyservice.model.SharedWith.TRUSTED);

    StoryInput input2 = new StoryInput();
    input2.setContent(java.util.List.of(java.util.Map.of("insert", "Story 2")));
    input2.setSharedWith(com.pallas.storyservice.model.SharedWith.COMMUNITY);

    // Act - Create two stories
    String response1 =
        mockMvc
            .perform(
                post("/stories")
                    .with(jwt().jwt(jwt -> jwt.claim("sub", AUTHENTICATED_MEMBER_ID.toString())))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(input1)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    String response2 =
        mockMvc
            .perform(
                post("/stories")
                    .with(jwt().jwt(jwt -> jwt.claim("sub", AUTHENTICATED_MEMBER_ID.toString())))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(input2)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // Assert - IDs are unique
    String id1 = objectMapper.readTree(response1).get("id").asText();
    String id2 = objectMapper.readTree(response2).get("id").asText();

    assertThat(id1).isNotEqualTo(id2);
    assertThat(storyRepository.count()).isEqualTo(2L);
  }

  @Test
  @DisplayName("POST /stories should set publishedAt timestamp on creation")
  void testCreateStorySetPublishedAtTimestamp() throws Exception {
    // Arrange
    StoryInput input = new StoryInput();
    input.setContent(java.util.List.of(java.util.Map.of("insert", "Timestamped story")));
    input.setSharedWith(com.pallas.storyservice.model.SharedWith.PUBLIC);

    // Act - Create story and note request time
    long beforeRequest = System.currentTimeMillis();
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
    long afterRequest = System.currentTimeMillis();

    // Extract story ID
    String storyId = objectMapper.readTree(response).get("id").asText();

    // Assert - publishedAt is set and within the request time window
    var entity = storyRepository.findById(UUID.fromString(storyId)).orElseThrow();
    assertThat(entity.getPublishedAt()).isNotNull();
    long publishedAt = entity.getPublishedAt().toInstant().toEpochMilli();
    assertThat(publishedAt).isGreaterThanOrEqualTo(beforeRequest - 1000); // 1s buffer
    assertThat(publishedAt).isLessThanOrEqualTo(afterRequest + 1000);
  }

  @Test
  @DisplayName("POST /stories should respect all shared with levels and persist to database")
  void testCreateStoryWithAllSharedWithLevels() throws Exception {
    com.pallas.storyservice.model.SharedWith[] levels = {
      com.pallas.storyservice.model.SharedWith.TRUSTED,
      com.pallas.storyservice.model.SharedWith.CONNECTED,
      com.pallas.storyservice.model.SharedWith.COMMUNITY,
      com.pallas.storyservice.model.SharedWith.PUBLIC
    };

    for (var level : levels) {
      // Arrange
      StoryInput input = new StoryInput();
      input.setContent(java.util.List.of(java.util.Map.of("insert", "Story with " + level)));
      input.setSharedWith(level);

      // Act & Assert
      String response =
          mockMvc
              .perform(
                  post("/stories")
                      .with(jwt().jwt(jwt -> jwt.claim("sub", AUTHENTICATED_MEMBER_ID.toString())))
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsString(input)))
              .andExpect(status().isCreated())
              .andExpect(jsonPath("$.sharedWith").value(level.toString()))
              .andReturn()
              .getResponse()
              .getContentAsString();

      // Verify persisted to database
      String storyId = objectMapper.readTree(response).get("id").asText();
      var entity = storyRepository.findById(UUID.fromString(storyId)).orElseThrow();
      assertThat(entity.getSharedWith().toString()).isEqualTo(level.toString());
    }
  }

  // =========================================================================
  // GET /stories/story/{storyId} - Get Story (Full Stack)
  // =========================================================================

  @Test
  @DisplayName("GET /stories/story/{storyId} should retrieve and deserialize persisted story")
  void testGetStoryRetrievesPersistedData() throws Exception {
    // Arrange - Create a story first
    StoryInput createInput = new StoryInput();
    createInput.setContent(java.util.List.of(java.util.Map.of("insert", "Get me")));
    createInput.setSharedWith(com.pallas.storyservice.model.SharedWith.PUBLIC);

    String createResponse =
        mockMvc
            .perform(
                post("/stories")
                    .with(jwt().jwt(jwt -> jwt.claim("sub", AUTHENTICATED_MEMBER_ID.toString())))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createInput)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    String storyId = objectMapper.readTree(createResponse).get("id").asText();

    // Act & Assert - Retrieve the story
    mockMvc
        .perform(
            get("/stories/story/{storyId}", storyId)
                .with(jwt().jwt(jwt -> jwt.claim("sub", AUTHENTICATED_MEMBER_ID.toString()))))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(storyId))
        .andExpect(jsonPath("$.authorId").value(AUTHENTICATED_MEMBER_ID.toString()))
        .andExpect(jsonPath("$.content[0].insert").value("Get me"))
        .andExpect(jsonPath("$.sharedWith").value("PUBLIC"))
        .andExpect(jsonPath("$.publishedAt").isNotEmpty());
  }

  @Test
  @DisplayName("GET /stories/story/{storyId} should return 404 for non-existent story")
  void testGetStoryNotFoundReturns404() throws Exception {
    // Arrange - Non-existent story ID
    String nonExistentId = "550e8400-e29b-41d4-a716-446655440000";

    // Act & Assert
    mockMvc
        .perform(
            get("/stories/story/{storyId}", nonExistentId)
                .with(jwt().jwt(jwt -> jwt.claim("sub", AUTHENTICATED_MEMBER_ID.toString()))))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("GET /stories/story/{storyId} should return 401 when not authenticated")
  void testGetStoryUnauthorizedWhenNotAuthenticated() throws Exception {
    // Arrange - Non-existent story ID (but testing auth first)
    String storyId = "550e8400-e29b-41d4-a716-446655440000";

    // Act & Assert
    mockMvc.perform(get("/stories/story/{storyId}", storyId)).andExpect(status().isUnauthorized());
  }

  // =========================================================================
  // Data Layer Specific Tests
  // =========================================================================

  @Test
  @DisplayName("Story persistence should survive repository transaction and be queryable")
  void testStoryPersistenceAcrossTransactions() throws Exception {
    // Arrange
    StoryInput input = new StoryInput();
    input.setContent(java.util.List.of(java.util.Map.of("insert", "Transactional test")));
    input.setSharedWith(com.pallas.storyservice.model.SharedWith.COMMUNITY);

    // Act - Create story
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

    String storyId = objectMapper.readTree(response).get("id").asText();

    // Assert - Verify persistence across the repository boundary
    assertThat(storyRepository.findById(UUID.fromString(storyId))).isPresent();

    // Assert - Verify we can retrieve and deserialize correctly
    mockMvc
        .perform(
            get("/stories/story/{storyId}", storyId)
                .with(jwt().jwt(jwt -> jwt.claim("sub", AUTHENTICATED_MEMBER_ID.toString()))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(storyId));
  }

  @Test
  @DisplayName("Story content JSON serialization should be lossless through persistence")
  void testContentJsonSerializationRoundTrip() throws Exception {
    // Arrange - Complex content with multiple operations
    StoryInput input = new StoryInput();
    input.setContent(
        java.util.List.of(
            java.util.Map.of("insert", "Bold text", "attributes", java.util.Map.of("bold", true)),
            java.util.Map.of("insert", " and "),
            java.util.Map.of(
                "insert", "italic text", "attributes", java.util.Map.of("italic", true))));
    input.setSharedWith(com.pallas.storyservice.model.SharedWith.TRUSTED);

    // Act - Create and retrieve
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

    // Assert - Verify content is preserved exactly
    String getResponse =
        mockMvc
            .perform(
                get("/stories/story/{storyId}", storyId)
                    .with(jwt().jwt(jwt -> jwt.claim("sub", AUTHENTICATED_MEMBER_ID.toString()))))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    var content = objectMapper.readTree(getResponse).get("content");
    assertThat(content.get(0).get("insert").asText()).isEqualTo("Bold text");
    assertThat(content.get(0).get("attributes").get("bold").asBoolean()).isTrue();
    assertThat(content.get(1).get("insert").asText()).isEqualTo(" and ");
    assertThat(content.get(2).get("insert").asText()).isEqualTo("italic text");
    assertThat(content.get(2).get("attributes").get("italic").asBoolean()).isTrue();
  }
}
