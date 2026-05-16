package com.pallas.memberservice.domain;

import java.util.UUID;

public class MemberNotFoundException extends RuntimeException {

  public MemberNotFoundException(UUID memberId) {
    super("Member not found: " + memberId);
  }
}
