package com.pallas.communityservice.api;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pallas.communityservice.domain.CircleType;
import com.pallas.communityservice.domain.ConnectionSuggestion;
import com.pallas.communityservice.domain.ConnectionSuggestionPort;
import com.pallas.communityservice.domain.MemberServicePort;
import com.pallas.communityservice.domain.SuggestionStatus;
import com.pallas.communityservice.model.ConnectionSuggestionInput;
import com.pallas.communityservice.model.ConnectionSuggestionResponse;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for ConnectionSuggestionsController.
 *
 * <p>Tests the full vertical stack: API → ApplicationService → Domain → Ports (mocked only).
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ConnectionSuggestionsControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private ConnectionSuggestionPort suggestionPort;
  @MockitoBean private MemberServicePort memberServicePort;

  private static final UUID CURRENT_USER_ID =
      UUID.fromString("11111111-1111-1111-1111-111111111111");
  private static final UUID TARGET_MEMBER_ID =
      UUID.fromString("22222222-2222-2222-2222-222222222222");
  private static final UUID SUGGESTION_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");

  @Test
  void createConnectionSuggestion_WithValidInput_Returns201() throws Exception {
    ConnectionSuggestionInput input = new ConnectionSuggestionInput();
    input.setTargetMemberId(TARGET_MEMBER_ID);
    input.setTargetCircle(com.pallas.communityservice.model.CircleType.TRUSTED);

    ConnectionSuggestion created =
        ConnectionSuggestion.builder()
            .id(SUGGESTION_ID)
            .initiatorId(CURRENT_USER_ID)
            .targetId(TARGET_MEMBER_ID)
            .targetCircle(CircleType.TRUSTED)
            .status(SuggestionStatus.PENDING)
            .createdAt(OffsetDateTime.now())
            .respondedAt(null)
            .build();

    when(memberServicePort.resolveCurrentMemberId(anyString())).thenReturn(CURRENT_USER_ID);
    when(suggestionPort.save(any(ConnectionSuggestion.class))).thenReturn(created);

    mockMvc
        .perform(
            post("/communities/connection-suggestions")
                .with(jwt().jwt(builder -> builder.subject(CURRENT_USER_ID.toString())))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"));
  }

  @Test
  void createConnectionSuggestion_WithMissingTargetMemberId_Returns400() throws Exception {
    ConnectionSuggestionInput input = new ConnectionSuggestionInput();
    input.setTargetCircle(com.pallas.communityservice.model.CircleType.TRUSTED);

    when(memberServicePort.resolveCurrentMemberId(anyString())).thenReturn(CURRENT_USER_ID);

    mockMvc
        .perform(
            post("/communities/connection-suggestions")
                .with(jwt().jwt(builder -> builder.subject(CURRENT_USER_ID.toString())))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void listConnectionSuggestions_WithPendingSuggestions_Returns200() throws Exception {
    when(memberServicePort.resolveCurrentMemberId(anyString())).thenReturn(CURRENT_USER_ID);

    OffsetDateTime now = OffsetDateTime.now();
    ConnectionSuggestion suggestion1 =
        ConnectionSuggestion.builder()
            .id(SUGGESTION_ID)
            .initiatorId(TARGET_MEMBER_ID)
            .targetId(CURRENT_USER_ID)
            .targetCircle(CircleType.TRUSTED)
            .status(SuggestionStatus.PENDING)
            .createdAt(now)
            .respondedAt(null)
            .build();

    when(suggestionPort.findPendingByParticipantId(CURRENT_USER_ID))
        .thenReturn(List.of(suggestion1));

    mockMvc
        .perform(
            get("/communities/connection-suggestions")
                .with(jwt().jwt(builder -> builder.subject(CURRENT_USER_ID.toString()))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.suggestions", hasSize(1)))
        .andExpect(jsonPath("$.suggestions[0].status", is("pending")));
  }

  @Test
  void listConnectionSuggestions_WithNoSuggestions_Returns200WithEmptyList() throws Exception {
    when(memberServicePort.resolveCurrentMemberId(anyString())).thenReturn(CURRENT_USER_ID);
    when(suggestionPort.findPendingByParticipantId(CURRENT_USER_ID)).thenReturn(List.of());

    mockMvc
        .perform(
            get("/communities/connection-suggestions")
                .with(jwt().jwt(builder -> builder.subject(CURRENT_USER_ID.toString()))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.suggestions", hasSize(0)));
  }

  @Test
  void respondToConnectionSuggestion_WithAcceptedDecision_Returns200() throws Exception {
    when(memberServicePort.resolveCurrentMemberId(anyString())).thenReturn(CURRENT_USER_ID);

    OffsetDateTime now = OffsetDateTime.now();
    ConnectionSuggestion suggestion =
        ConnectionSuggestion.builder()
            .id(SUGGESTION_ID)
            .initiatorId(TARGET_MEMBER_ID)
            .targetId(CURRENT_USER_ID)
            .targetCircle(CircleType.TRUSTED)
            .status(SuggestionStatus.PENDING)
            .createdAt(now)
            .respondedAt(null)
            .build();

    ConnectionSuggestion responded =
        ConnectionSuggestion.builder()
            .id(SUGGESTION_ID)
            .initiatorId(TARGET_MEMBER_ID)
            .targetId(CURRENT_USER_ID)
            .targetCircle(CircleType.TRUSTED)
            .status(SuggestionStatus.ACCEPTED)
            .createdAt(now)
            .respondedAt(OffsetDateTime.now())
            .build();

    when(suggestionPort.findById(SUGGESTION_ID)).thenReturn(Optional.of(suggestion));
    when(suggestionPort.update(any(ConnectionSuggestion.class))).thenReturn(responded);

    ConnectionSuggestionResponse request = new ConnectionSuggestionResponse();
    request.setDecision(com.pallas.communityservice.model.SuggestionDecision.ACCEPTED);

    mockMvc
        .perform(
            put("/communities/connection-suggestions/{id}/response", SUGGESTION_ID)
                .with(jwt().jwt(builder -> builder.subject(CURRENT_USER_ID.toString())))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());
  }

  @Test
  void respondToConnectionSuggestion_WhenSuggestionNotFound_Returns404() throws Exception {
    when(memberServicePort.resolveCurrentMemberId(anyString())).thenReturn(CURRENT_USER_ID);
    when(suggestionPort.findById(SUGGESTION_ID)).thenReturn(Optional.empty());

    ConnectionSuggestionResponse request = new ConnectionSuggestionResponse();
    request.setDecision(com.pallas.communityservice.model.SuggestionDecision.ACCEPTED);

    mockMvc
        .perform(
            put("/communities/connection-suggestions/{id}/response", SUGGESTION_ID)
                .with(jwt().jwt(builder -> builder.subject(CURRENT_USER_ID.toString())))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound());
  }

  @Test
  void respondToConnectionSuggestion_WithMissingDecision_Returns400() throws Exception {
    when(memberServicePort.resolveCurrentMemberId(anyString())).thenReturn(CURRENT_USER_ID);

    ConnectionSuggestionResponse request = new ConnectionSuggestionResponse();

    mockMvc
        .perform(
            put("/communities/connection-suggestions/{id}/response", SUGGESTION_ID)
                .with(jwt().jwt(builder -> builder.subject(CURRENT_USER_ID.toString())))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }
}
