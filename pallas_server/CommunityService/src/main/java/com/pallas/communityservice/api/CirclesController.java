package com.pallas.communityservice.api;

import com.pallas.communityservice.model.Circles;
import lombok.CustomLog;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/** REST controller implementing the generated {@link CirclesApi} interface. */
@CustomLog
@RestController
public class CirclesController implements CirclesApi {

  @Override
  public ResponseEntity<Circles> getMyCircles() {
    log.info("GET /me/circles");
    throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
  }
}
