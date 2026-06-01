package com.pallas.communityservice.api;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pallas.communityservice.domain.CircleMembership;
import com.pallas.communityservice.domain.CircleMembershipPort;
import com.pallas.communityservice.domain.CircleType;
import com.pallas.communityservice.domain.MemberServicePort;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RelationshipsControllerTest {

  @Autowired private MockMvc mockMvc;
  // Real JPA adapter — writes and reads go through H2 in-memory DB
  @Autowired private CircleMembershipPort circleMembershipPort;
  // Mocked because it makes an outbound HTTP call to the Member Service
  @MockitoBean private MemberServicePort memberServicePort;

  @BeforeEach
  void setUp() {
    // Setup mock to return authenticated user
    when(memberServicePort.resolveCurrentMemberId(anyString()))
        .thenReturn(UUID.fromString("11111111-1111-1111-1111-111111111111"));
  }

  @Test
  void getRelationship_WithTrustedCircleMembership_ReturnsTrustedRelationshipType()
      throws Exception {
    // Given: Two members with a trusted circle membership
    UUID currentMemberId = UUID.fromString("11111111-1111-1111-1111-111111111111");
    UUID targetMemberId = UUID.fromString("22222222-2222-2222-2222-222222222222");

    // Ensure canonical ordering: member with smaller UUID is A
    UUID memberIdA;
    UUID memberIdB;
    if (currentMemberId.compareTo(targetMemberId) < 0) {
      memberIdA = currentMemberId;
      memberIdB = targetMemberId;
    } else {
      memberIdA = targetMemberId;
      memberIdB = currentMemberId;
    }

    CircleMembership membership =
        CircleMembership.builder()
            .memberIdA(memberIdA)
            .memberIdB(memberIdB)
            .circleType(CircleType.TRUSTED)
            .memberSince(OffsetDateTime.now())
            .build();
    circleMembershipPort.save(membership);

    // When/Then: Query relationship returns TRUSTED
    mockMvc
        .perform(get("/communities/members/{memberId}/relationship", targetMemberId).with(jwt()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.memberId").value(targetMemberId.toString()))
        .andExpect(jsonPath("$.relationshipType").value("trusted"));
  }
}
