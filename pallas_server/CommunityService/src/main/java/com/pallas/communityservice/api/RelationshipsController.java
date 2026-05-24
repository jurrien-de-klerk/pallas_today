package com.pallas.communityservice.api;

import com.pallas.communityservice.application.CommunityApplicationService;
import com.pallas.communityservice.domain.RelationshipType;
import com.pallas.communityservice.model.Relationship;
import java.util.UUID;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/** REST controller implementing the generated {@link RelationshipsApi} interface. */
@CustomLog
@RestController
@RequiredArgsConstructor
public class RelationshipsController implements RelationshipsApi {

  private final CommunityApplicationService applicationService;

  @Override
  public ResponseEntity<Relationship> getRelationship(UUID memberId) {
    log.info("GET /communities/members/{memberId}/relationship");
    RelationshipType type = applicationService.getRelationship(memberId);
    Relationship response = new Relationship();
    response.setMemberId(memberId);
    response.setRelationshipType(toModel(type));
    return ResponseEntity.ok(response);
  }

  private com.pallas.communityservice.model.RelationshipType toModel(RelationshipType domain) {
    return switch (domain) {
      case TRUSTED -> com.pallas.communityservice.model.RelationshipType.TRUSTED;
      case CONNECTED -> com.pallas.communityservice.model.RelationshipType.CONNECTED;
      case COMMUNITY -> com.pallas.communityservice.model.RelationshipType.COMMUNITY;
    };
  }
}
