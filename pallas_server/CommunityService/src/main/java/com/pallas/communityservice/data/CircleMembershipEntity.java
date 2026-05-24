package com.pallas.communityservice.data;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "circle_membership")
@Getter
@Setter
@NoArgsConstructor
public class CircleMembershipEntity {

  @EmbeddedId private CircleMembershipId id;

  @Enumerated(EnumType.STRING)
  @Column(name = "circle_type", nullable = false, length = 50)
  private CircleTypeEntity circleType;

  @Column(name = "member_since", nullable = false, updatable = false)
  private OffsetDateTime memberSince;
}
