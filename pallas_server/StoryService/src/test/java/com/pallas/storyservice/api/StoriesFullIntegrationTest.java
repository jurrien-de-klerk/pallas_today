package com.pallas.storyservice.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pallas.integrations.member.MemberServicePort;
import com.pallas.storyservice.data.StoryEntity;
import com.pallas.storyservice.data.StoryRepository;
import com.pallas.storyservice.domain.CommunityServicePort;
import com.pallas.storyservice.model.SharedWith;
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
 * Full stack integration tests for Story API endpoints.
 *
 * <p>Tests the entire stack from HTTP API request through application and domain layers all the way
 * to database persistence. Uses H2 in-memory database configured via application-test.properties.
 *
 * <p>These tests verify that:
 *
 * <ul>
 *   <li>API serializes/deserializes content correctly (Quill Delta arrays)
 *   <li>Application layer handles authentication and business logic
 *   <li>Domain layer enforces access control and generates timestamps
 *   <li>Data layer persists stories to database with correct entity mapping
 *   <li>Database roundtrip preserves all story data without loss
 * </ul>
 *
 * <p>This layer catches bugs that mock-based @WebMvcTest cannot detect, such as Hibernate mapping
 * issues, optimistic locking conflicts, and database constraint violations.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Story Full Stack Integration Tests")
class StoriesFullIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private StoryRepository repository;

  @MockitoBean private MemberServicePort memberServicePort;

  @MockitoBean private CommunityServicePort communityServicePort;

  private static final UUID CURRENT_USER_ID =
      UUID.fromString("550e8400-e29b-41d4-a716-446655440001");

  @BeforeEach
  void setUp() {
    repository.deleteAll();
    // Mock member service to return the current user ID for any bearer token
    when(memberServicePort.resolveCurrentMemberId(anyString())).thenReturn(CURRENT_USER_ID);
    // Mock community service to allow access for any relationship checks
    when(communityServicePort.getRelationship(any(UUID.class), anyString()))
        .thenReturn(com.pallas.storyservice.domain.RelationshipType.TRUSTED);
  }

  // =========================================================================
  // POST /stories - Create Story (Full Stack)
  // =========================================================================

  @Test
  @DisplayName(
      "POST /stories should persist story to database and return 201 with all fields populated")
  void testCreateStoryPersistsToDatabase() throws Exception {
    // Arrange - Create API request with Quill Delta content
    List<Object> quillDelta =
        List.of(Map.of("insert", "First paragraph\n"), Map.of("insert", "Second paragraph\n"));

    Map<String, Object> storyInput =
        Map.of("content", quillDelta, "sharedWith", SharedWith.TRUSTED.toString());

    // Act - Make POST request
    String response =
        mockMvc
            .perform(
                post("/stories")
                    .with(jwt().jwt(jwt -> jwt.claim("sub", CURRENT_USER_ID.toString())))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(storyInput)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.authorId").value(CURRENT_USER_ID.toString()))
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content[0].insert").value("First paragraph\n"))
            .andExpect(jsonPath("$.content[1].insert").value("Second paragraph\n"))
            .andExpect(jsonPath("$.sharedWith").value("TRUSTED"))
            .andExpect(jsonPath("$.publishedAt").exists())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // Extract the story ID from response
    String storyIdStr = objectMapper.readTree(response).get("id").asText();
    UUID storyId = UUID.fromString(storyIdStr);

    // Assert - Verify story was actually persisted to database
    assertThat(repository.findById(storyId)).isPresent();
    StoryEntity persisted = repository.findById(storyId).orElseThrow();
    assertThat(persisted.getId()).isEqualTo(storyId);
    assertThat(persisted.getAuthorId()).isEqualTo(CURRENT_USER_ID);
    // Compare by string value since domain and JPA enums are different types
    assertThat(persisted.getSharedWith().toString())
        .isEqualTo(com.pallas.storyservice.domain.SharedWith.TRUSTED.toString());
    assertThat(persisted.getContent()).contains("First paragraph");
    assertThat(persisted.getContent()).contains("Second paragraph");
    assertThat(persisted.getPublishedAt()).isNotNull();
  }

  @Test
  @DisplayName("POST /stories should generate unique ID for each story")
  void testCreateStoryGeneratesUniqueIds() throws Exception {
    // Arrange
    List<Object> quillDelta = List.of(Map.of("insert", "Story A\n"));
    Map<String, Object> storyInput =
        Map.of("content", quillDelta, "sharedWith", SharedWith.PUBLIC.toString());

    // Act & Assert - Create first story
    String response1 =
        mockMvc
            .perform(
                post("/stories")
                    .with(jwt().jwt(jwt -> jwt.claim("sub", CURRENT_USER_ID.toString())))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(storyInput)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    String storyId1 = objectMapper.readTree(response1).get("id").asText();

    // Update input for second story
    quillDelta = List.of(Map.of("insert", "Story B\n"));
    storyInput = Map.of("content", quillDelta, "sharedWith", SharedWith.PUBLIC.toString());

    // Act & Assert - Create second story
    String response2 =
        mockMvc
            .perform(
                post("/stories")
                    .with(jwt().jwt(jwt -> jwt.claim("sub", CURRENT_USER_ID.toString())))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(storyInput)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    String storyId2 = objectMapper.readTree(response2).get("id").asText();

    // Assert - Both stories have unique IDs
    assertThat(storyId1).isNotEqualTo(storyId2);
    assertThat(repository.count()).isEqualTo(2);
  }

  @Test
  @DisplayName("POST /stories should persist all SharedWith levels correctly")
  void testCreateStoryPersistsAllSharedWithLevels() throws Exception {
    // Arrange - Test each SharedWith level
    com.pallas.storyservice.model.SharedWith[] levels = {
      com.pallas.storyservice.model.SharedWith.TRUSTED,
      com.pallas.storyservice.model.SharedWith.COMMUNITY,
      com.pallas.storyservice.model.SharedWith.PUBLIC
    };

    List<Object> quillDelta = List.of(Map.of("insert", "Test content\n"));

    for (com.pallas.storyservice.model.SharedWith level : levels) {
      // Act
      Map<String, Object> storyInput =
          Map.of("content", quillDelta, "sharedWith", level.toString());

      String response =
          mockMvc
              .perform(
                  post("/stories")
                      .with(jwt().jwt(jwt -> jwt.claim("sub", CURRENT_USER_ID.toString())))
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsString(storyInput)))
              .andExpect(status().isCreated())
              .andReturn()
              .getResponse()
              .getContentAsString();

      String storyId = objectMapper.readTree(response).get("id").asText();

      // Assert - Verify SharedWith level persisted correctly (compare by string value)
      StoryEntity persisted = repository.findById(UUID.fromString(storyId)).orElseThrow();
      assertThat(persisted.getSharedWith().toString()).isEqualTo(level.toString());
    }

    assertThat(repository.count()).isEqualTo(3);
  }

  @Test
  @DisplayName("POST /stories should set publishedAt timestamp")
  void testCreateStorySetPublishedAtTimestamp() throws Exception {
    // Arrange
    List<Object> quillDelta = List.of(Map.of("insert", "Timestamped story\n"));
    Map<String, Object> storyInput =
        Map.of("content", quillDelta, "sharedWith", SharedWith.TRUSTED.toString());

    long beforeTimestampMs = System.currentTimeMillis();

    // Act
    String response =
        mockMvc
            .perform(
                post("/stories")
                    .with(jwt().jwt(jwt -> jwt.claim("sub", CURRENT_USER_ID.toString())))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(storyInput)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    long afterTimestampMs = System.currentTimeMillis();

    String storyId = objectMapper.readTree(response).get("id").asText();

    // Assert - Verify publishedAt is set and within reasonable time bounds
    StoryEntity persisted = repository.findById(UUID.fromString(storyId)).orElseThrow();
    assertThat(persisted.getPublishedAt()).isNotNull();
    long persistedTimeMs = persisted.getPublishedAt().toInstant().toEpochMilli();
    assertThat(persistedTimeMs).isGreaterThanOrEqualTo(beforeTimestampMs);
    assertThat(persistedTimeMs).isLessThanOrEqualTo(afterTimestampMs + 1000); // Allow 1s tolerance
  }

  @Test
  @DisplayName("POST /stories should handle complex Quill Delta content correctly")
  void testContentJsonSerializationRoundTrip() throws Exception {
    // Arrange - Complex Quill Delta with multiple operations
    List<Object> quillDelta =
        List.of(
            Map.of("insert", "Bold text", "attributes", Map.of("bold", true)),
            Map.of("insert", " and italic", "attributes", Map.of("italic", true)),
            Map.of("insert", "\n"),
            Map.of("insert", "Link", "attributes", Map.of("link", "https://example.com")),
            Map.of("insert", "\n"));

    Map<String, Object> storyInput =
        Map.of("content", quillDelta, "sharedWith", SharedWith.COMMUNITY.toString());

    // Act
    String response =
        mockMvc
            .perform(
                post("/stories")
                    .with(jwt().jwt(jwt -> jwt.claim("sub", CURRENT_USER_ID.toString())))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(storyInput)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.content[0].insert").value("Bold text"))
            .andExpect(jsonPath("$.content[0].attributes.bold").value(true))
            .andExpect(jsonPath("$.content[1].insert").value(" and italic"))
            .andExpect(jsonPath("$.content[1].attributes.italic").value(true))
            .andExpect(jsonPath("$.content[3].attributes.link").value("https://example.com"))
            .andReturn()
            .getResponse()
            .getContentAsString();

    String storyId = objectMapper.readTree(response).get("id").asText();

    // Assert - Verify complex content survived database roundtrip
    StoryEntity persisted = repository.findById(UUID.fromString(storyId)).orElseThrow();
    String persistedJson = persisted.getContent();
    assertThat(persistedJson).contains("\"bold\":true");
    assertThat(persistedJson).contains("\"italic\":true");
    assertThat(persistedJson).contains("https://example.com");

    // Verify by fetching through API
    mockMvc
        .perform(
            get("/stories/story/" + storyId)
                .with(jwt().jwt(jwt -> jwt.claim("sub", CURRENT_USER_ID.toString()))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].insert").value("Bold text"))
        .andExpect(jsonPath("$.content[0].attributes.bold").value(true));
  }

  // =========================================================================
  // GET /stories/{storyId} - Retrieve Story (Full Stack)
  // =========================================================================

  @Test
  @DisplayName("GET /stories/{id} should retrieve persisted story with all data intact")
  void testGetStoryRetrievesPersistedData() throws Exception {
    // Arrange - Create and persist a story first
    List<Object> quillDelta = List.of(Map.of("insert", "Retrievable story\n"));
    Map<String, Object> storyInput =
        Map.of("content", quillDelta, "sharedWith", SharedWith.TRUSTED.toString());

    String createResponse =
        mockMvc
            .perform(
                post("/stories")
                    .with(jwt().jwt(jwt -> jwt.claim("sub", CURRENT_USER_ID.toString())))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(storyInput)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    String storyId = objectMapper.readTree(createResponse).get("id").asText();

    // Act & Assert - Retrieve the story via GET
    mockMvc
        .perform(
            get("/stories/story/" + storyId)
                .with(jwt().jwt(jwt -> jwt.claim("sub", CURRENT_USER_ID.toString()))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(storyId))
        .andExpect(jsonPath("$.authorId").value(CURRENT_USER_ID.toString()))
        .andExpect(jsonPath("$.content[0].insert").value("Retrievable story\n"))
        .andExpect(jsonPath("$.sharedWith").value("TRUSTED"))
        .andExpect(jsonPath("$.publishedAt").exists());
  }

  @Test
  @DisplayName("GET /stories/{id} should return 404 when story not found")
  void testGetStoryNotFoundReturns404() throws Exception {
    // Arrange
    UUID nonExistentId = UUID.randomUUID();

    // Act & Assert
    mockMvc
        .perform(
            get("/stories/story/" + nonExistentId)
                .with(jwt().jwt(jwt -> jwt.claim("sub", CURRENT_USER_ID.toString()))))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("GET /stories/{id} should return 401 when not authenticated")
  void testGetStoryUnauthorizedWhenNotAuthenticated() throws Exception {
    // Arrange - Create a story first
    List<Object> quillDelta = List.of(Map.of("insert", "Protected story\n"));
    Map<String, Object> storyInput =
        Map.of("content", quillDelta, "sharedWith", SharedWith.TRUSTED.toString());

    String createResponse =
        mockMvc
            .perform(
                post("/stories")
                    .with(jwt().jwt(jwt -> jwt.claim("sub", CURRENT_USER_ID.toString())))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(storyInput)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    String storyId = objectMapper.readTree(createResponse).get("id").asText();

    // Act & Assert - GET without authentication
    mockMvc.perform(get("/stories/story/" + storyId)).andExpect(status().isUnauthorized());
  }

  // =========================================================================
  // Persistence Behavior Tests
  // =========================================================================

  @Test
  @DisplayName("Story persistence should survive across multiple transactions")
  void testStoryPersistenceAcrossTransactions() throws Exception {
    // Arrange - Create story in first transaction
    List<Object> quillDelta = List.of(Map.of("insert", "Persistent story\n"));
    Map<String, Object> storyInput =
        Map.of("content", quillDelta, "sharedWith", SharedWith.PUBLIC.toString());

    String createResponse =
        mockMvc
            .perform(
                post("/stories")
                    .with(jwt().jwt(jwt -> jwt.claim("sub", CURRENT_USER_ID.toString())))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(storyInput)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    String storyId = objectMapper.readTree(createResponse).get("id").asText();

    // Act - Retrieve in separate transaction
    mockMvc
        .perform(
            get("/stories/story/" + storyId)
                .with(jwt().jwt(jwt -> jwt.claim("sub", CURRENT_USER_ID.toString()))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(storyId));

    // Assert - Verify via direct database query
    StoryEntity persisted = repository.findById(UUID.fromString(storyId)).orElseThrow();
    assertThat(persisted.getId()).isEqualTo(UUID.fromString(storyId));
    assertThat(persisted.getAuthorId()).isEqualTo(CURRENT_USER_ID);
  }
}
