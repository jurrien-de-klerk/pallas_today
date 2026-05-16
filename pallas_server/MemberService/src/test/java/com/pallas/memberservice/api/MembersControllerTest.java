package com.pallas.memberservice.api;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pallas.memberservice.config.SecurityConfig;
import com.pallas.memberservice.domain.Member;
import com.pallas.memberservice.domain.MemberNotFoundException;
import com.pallas.memberservice.domain.MemberService;
import com.pallas.memberservice.exception.GlobalExceptionHandler;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MembersController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
class MembersControllerTest {

  private static final UUID MEMBER_ID = UUID.fromString("987fcdeb-51a2-43d7-b012-345678901234");
  private static final String KEYCLOAK_SUB = "user-sub-123";
  private static final Member MEMBER = new Member(MEMBER_ID, "Alice", "Smith");

  @Autowired private MockMvc mockMvc;

  @MockBean private MemberService memberService;

  // --- GET /members/me ---

  @Test
  void getAuthenticatedMember_returnsProfile_whenAuthenticated() throws Exception {
    when(memberService.getCurrentMember(KEYCLOAK_SUB)).thenReturn(MEMBER);

    mockMvc
        .perform(get("/members/me").with(jwt().jwt(j -> j.subject(KEYCLOAK_SUB))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.memberId").value(MEMBER_ID.toString()))
        .andExpect(jsonPath("$.firstName").value("Alice"))
        .andExpect(jsonPath("$.lastName").value("Smith"));
  }

  @Test
  void getAuthenticatedMember_returns401_whenNoToken() throws Exception {
    mockMvc.perform(get("/members/me")).andExpect(status().isUnauthorized());
  }

  // --- GET /members/member/{memberId} ---

  @Test
  void getMember_returnsMember_whenFound() throws Exception {
    when(memberService.getMember(MEMBER_ID)).thenReturn(MEMBER);

    mockMvc
        .perform(get("/members/member/{id}", MEMBER_ID).with(jwt()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.memberId").value(MEMBER_ID.toString()))
        .andExpect(jsonPath("$.firstName").value("Alice"))
        .andExpect(jsonPath("$.lastName").value("Smith"));
  }

  @Test
  void getMember_returns404_whenNotFound() throws Exception {
    when(memberService.getMember(MEMBER_ID)).thenThrow(new MemberNotFoundException(MEMBER_ID));

    mockMvc
        .perform(get("/members/member/{id}", MEMBER_ID).with(jwt()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Member not found: " + MEMBER_ID));
  }

  @Test
  void getMember_returns401_whenNoToken() throws Exception {
    mockMvc.perform(get("/members/member/{id}", MEMBER_ID)).andExpect(status().isUnauthorized());
  }

  // --- GET /members/batch ---

  @Test
  void getMembers_returnsBatch_whenAllFound() throws Exception {
    UUID id2 = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    Member member2 = new Member(id2, "Bob", "Jones");
    when(memberService.getMembers(List.of(MEMBER_ID, id2))).thenReturn(List.of(MEMBER, member2));

    mockMvc
        .perform(
            get("/members/batch")
                .param("memberId", MEMBER_ID.toString())
                .param("memberId", id2.toString())
                .with(jwt()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.members.length()").value(2))
        .andExpect(jsonPath("$.members[0].memberId").value(MEMBER_ID.toString()))
        .andExpect(jsonPath("$.members[1].memberId").value(id2.toString()));
  }

  @Test
  void getMembers_returnsEmptyBatch_whenNoneFound() throws Exception {
    when(memberService.getMembers(List.of(MEMBER_ID))).thenReturn(List.of());

    mockMvc
        .perform(get("/members/batch").param("memberId", MEMBER_ID.toString()).with(jwt()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.members.length()").value(0));
  }

  @Test
  void getMembers_returns401_whenNoToken() throws Exception {
    mockMvc
        .perform(get("/members/batch").param("memberId", MEMBER_ID.toString()))
        .andExpect(status().isUnauthorized());
  }
}
