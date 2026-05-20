package com.pallas.communityservice.api;

import com.pallas.communityservice.model.Relationship;
import lombok.CustomLog;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/** REST controller implementing the generated {@link RelationshipsApi} interface. */
@CustomLog
@RestController
public class RelationshipsController implements RelationshipsApi {

  @Override
  public ResponseEntity<Relationship> getRelationship(String memberId) {
    log.info("GET /members/{memberId}/relationship");
    throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
  }
}
