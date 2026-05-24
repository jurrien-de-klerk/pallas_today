package com.pallas.communityservice.data;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CircleMembershipId implements Serializable {

  @Column(name = "member_id_a", nullable = false)
  private UUID memberIdA;

  @Column(name = "member_id_b", nullable = false)
  private UUID memberIdB;
}
