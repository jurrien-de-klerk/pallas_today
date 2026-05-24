package com.pallas.communityservice.api;

import com.pallas.communityservice.application.CommunityApplicationService;
import com.pallas.communityservice.domain.CircleMembership;
import com.pallas.communityservice.model.Circles;
import com.pallas.communityservice.model.MemberReference;
import java.util.List;
import java.util.UUID;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/** REST controller implementing the generated {@link CirclesApi} interface. */
@CustomLog
@RestController
@RequiredArgsConstructor
public class CirclesController implements CirclesApi {

  private final CommunityApplicationService applicationService;

  @Override
  public ResponseEntity<Circles> getMyCircles() {
    log.info("GET /communities/me/circles");
    CommunityApplicationService.Circles circles = applicationService.getMyCircles();
    Circles response = new Circles();
    response.setTrustedCircle(toMemberReferences(circles.trusted()));
    response.setConnectedCircle(toMemberReferences(circles.connected()));
    return ResponseEntity.ok(response);
  }

  private List<MemberReference> toMemberReferences(List<CircleMembership> memberships) {
    // The circles are already filtered per member by the application service.
    // We simply extract the partner ID from each membership.
    UUID currentUuid = applicationService.getCurrentUserUuid();
    return memberships.stream()
        .map(
            m -> {
              MemberReference ref = new MemberReference();
              ref.setMemberId(m.partnerIdFor(currentUuid));
              return ref;
            })
        .toList();
  }
}
