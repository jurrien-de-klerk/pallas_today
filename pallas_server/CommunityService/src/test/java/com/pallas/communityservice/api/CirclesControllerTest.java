package com.pallas.communityservice.api;

import static org.hamcrest.Matchers.hasSize;
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
import java.util.List;
import java.util.UUID;
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
class CirclesControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private CircleMembershipPort circleMembershipPort;
  @MockitoBean private MemberServicePort memberServicePort;

  private static final UUID CURRENT_USER_ID =
      UUID.fromString("11111111-1111-1111-1111-111111111111");
  private static final UUID TRUSTED_MEMBER_ID =
      UUID.fromString("33333333-3333-3333-3333-333333333333");
  private static final UUID CONNECTED_MEMBER_ID =
      UUID.fromString("44444444-4444-4444-4444-444444444444");

  @Test
  void getCircles_WithBothTrustedAndConnected_Returns200() throws Exception {
    CircleMembership trustedMembership =
        createCircleMembership(CURRENT_USER_ID, TRUSTED_MEMBER_ID, CircleType.TRUSTED);
    CircleMembership connectedMembership =
        createCircleMembership(CURRENT_USER_ID, CONNECTED_MEMBER_ID, CircleType.CONNECTED);

    when(memberServicePort.resolveCurrentMemberId(anyString())).thenReturn(CURRENT_USER_ID);
    when(circleMembershipPort.findAllByMemberIdAndCircleType(CURRENT_USER_ID, CircleType.TRUSTED))
        .thenReturn(List.of(trustedMembership));
    when(circleMembershipPort.findAllByMemberIdAndCircleType(CURRENT_USER_ID, CircleType.CONNECTED))
        .thenReturn(List.of(connectedMembership));

    mockMvc
        .perform(
            get("/communities/me/circles")
                .with(jwt().jwt(jwt -> jwt.claim("sub", CURRENT_USER_ID.toString()))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.trustedCircle", hasSize(1)))
        .andExpect(jsonPath("$.trustedCircle[0].memberId").value(TRUSTED_MEMBER_ID.toString()))
        .andExpect(jsonPath("$.connectedCircle", hasSize(1)))
        .andExpect(jsonPath("$.connectedCircle[0].memberId").value(CONNECTED_MEMBER_ID.toString()));
  }

  @Test
  void getCircles_WithTrustedOnly_Returns200() throws Exception {
    CircleMembership trustedMembership =
        createCircleMembership(CURRENT_USER_ID, TRUSTED_MEMBER_ID, CircleType.TRUSTED);

    when(memberServicePort.resolveCurrentMemberId(anyString())).thenReturn(CURRENT_USER_ID);
    when(circleMembershipPort.findAllByMemberIdAndCircleType(CURRENT_USER_ID, CircleType.TRUSTED))
        .thenReturn(List.of(trustedMembership));
    when(circleMembershipPort.findAllByMemberIdAndCircleType(CURRENT_USER_ID, CircleType.CONNECTED))
        .thenReturn(List.of());

    mockMvc
        .perform(
            get("/communities/me/circles")
                .with(jwt().jwt(jwt -> jwt.claim("sub", CURRENT_USER_ID.toString()))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.trustedCircle", hasSize(1)))
        .andExpect(jsonPath("$.trustedCircle[0].memberId").value(TRUSTED_MEMBER_ID.toString()))
        .andExpect(jsonPath("$.connectedCircle", hasSize(0)));
  }

  @Test
  void getCircles_WithConnectedOnly_Returns200() throws Exception {
    CircleMembership connectedMembership =
        createCircleMembership(CURRENT_USER_ID, CONNECTED_MEMBER_ID, CircleType.CONNECTED);

    when(memberServicePort.resolveCurrentMemberId(anyString())).thenReturn(CURRENT_USER_ID);
    when(circleMembershipPort.findAllByMemberIdAndCircleType(CURRENT_USER_ID, CircleType.TRUSTED))
        .thenReturn(List.of());
    when(circleMembershipPort.findAllByMemberIdAndCircleType(CURRENT_USER_ID, CircleType.CONNECTED))
        .thenReturn(List.of(connectedMembership));

    mockMvc
        .perform(
            get("/communities/me/circles")
                .with(jwt().jwt(jwt -> jwt.claim("sub", CURRENT_USER_ID.toString()))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.trustedCircle", hasSize(0)))
        .andExpect(jsonPath("$.connectedCircle", hasSize(1)))
        .andExpect(jsonPath("$.connectedCircle[0].memberId").value(CONNECTED_MEMBER_ID.toString()));
  }

  @Test
  void getCircles_WithBothEmptyCircles_Returns200() throws Exception {
    when(memberServicePort.resolveCurrentMemberId(anyString())).thenReturn(CURRENT_USER_ID);
    when(circleMembershipPort.findAllByMemberIdAndCircleType(CURRENT_USER_ID, CircleType.TRUSTED))
        .thenReturn(List.of());
    when(circleMembershipPort.findAllByMemberIdAndCircleType(CURRENT_USER_ID, CircleType.CONNECTED))
        .thenReturn(List.of());

    mockMvc
        .perform(
            get("/communities/me/circles")
                .with(jwt().jwt(jwt -> jwt.claim("sub", CURRENT_USER_ID.toString()))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.trustedCircle", hasSize(0)))
        .andExpect(jsonPath("$.connectedCircle", hasSize(0)));
  }

  private CircleMembership createCircleMembership(
      UUID memberId1, UUID memberId2, CircleType circleType) {
    UUID memberIdA;
    UUID memberIdB;
    if (memberId1.toString().compareTo(memberId2.toString()) < 0) {
      memberIdA = memberId1;
      memberIdB = memberId2;
    } else {
      memberIdA = memberId2;
      memberIdB = memberId1;
    }
    return CircleMembership.builder()
        .memberIdA(memberIdA)
        .memberIdB(memberIdB)
        .circleType(circleType)
        .memberSince(OffsetDateTime.now())
        .build();
  }
}
