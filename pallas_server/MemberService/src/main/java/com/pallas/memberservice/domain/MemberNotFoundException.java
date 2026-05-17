package com.pallas.memberservice.domain;

import java.util.UUID;

public class MemberNotFoundException extends RuntimeException {

  public MemberNotFoundException(UUID memberId) {
    super("Member not found: " + memberId);
  }

  /** Use when the identity provider has no profile for a known mapping's subject. */
  public MemberNotFoundException(String message) {
    super(message);
  }
}
