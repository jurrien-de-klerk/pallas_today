package com.pallas.memberservice.api;

import com.pallas.memberservice.domain.MemberService;
import com.pallas.memberservice.model.MemberBatch;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

/** REST controller implementing the generated {@link MembersApi} interface. */
@Log4j2
@RestController
@RequiredArgsConstructor
public class MembersController implements MembersApi {

  private final MemberService memberService;

  @Override
  public ResponseEntity<com.pallas.memberservice.model.Member> getAuthenticatedMember() {
    log.info("GET /members/me");
    Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return ResponseEntity.ok(toModel(memberService.getCurrentMember(jwt.getSubject())));
  }

  @Override
  public ResponseEntity<com.pallas.memberservice.model.Member> getMember(UUID memberId) {
    log.info("GET /members/member/{id}");
    return ResponseEntity.ok(toModel(memberService.getMember(memberId)));
  }

  @Override
  public ResponseEntity<MemberBatch> getMembers(List<UUID> memberId) {
    if (Objects.isNull(memberId) || memberId.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    log.info("GET /members/batch ({} ids requested)", memberId.size());
    List<com.pallas.memberservice.model.Member> members =
        memberService.getMembers(memberId).stream().map(this::toModel).toList();
    MemberBatch batch = new MemberBatch();
    batch.setMembers(members);
    return ResponseEntity.ok(batch);
  }

  private com.pallas.memberservice.model.Member toModel(
      com.pallas.memberservice.domain.Member domain) {
    com.pallas.memberservice.model.Member model = new com.pallas.memberservice.model.Member();
    model.setMemberId(domain.getMemberId());
    model.setFirstName(domain.getFirstName());
    model.setLastName(domain.getLastName());
    return model;
  }
}
