package com.pallas.memberservice.domain;

import java.util.UUID;
import lombok.Value;

/** A member as known to the Pallas domain. Keycloak details are not exposed here. */
@Value
public class Member {
  UUID memberId;
  String firstName;
  String lastName;
}
