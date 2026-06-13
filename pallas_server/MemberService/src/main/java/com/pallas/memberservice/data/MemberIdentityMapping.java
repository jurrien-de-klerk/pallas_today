package com.pallas.memberservice.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member_identity_mapping")
@Getter
@NoArgsConstructor(force = true)
public class MemberIdentityMapping {

  @Id
  @Column(name = "member_id", nullable = false, updatable = false)
  private final UUID memberId;

  @Column(name = "keycloak_sub", nullable = false, updatable = false, unique = true)
  private final String keycloakSub;

  @Column(name = "created_at", nullable = false, updatable = false)
  private final OffsetDateTime createdAt;

  public MemberIdentityMapping(UUID memberId, String keycloakSub, OffsetDateTime createdAt) {
    this.memberId = memberId;
    this.keycloakSub = keycloakSub;
    this.createdAt = createdAt;
  }
}
