package com.pallas.communityservice.data;

import com.pallas.communityservice.domain.CircleMembership;
import com.pallas.communityservice.domain.CircleMembershipPort;
import com.pallas.communityservice.domain.CircleType;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.CustomLog;
import org.springframework.stereotype.Component;

/** JPA adapter implementing {@link CircleMembershipPort}. */
@CustomLog
@Component
public class JpaCircleMembershipAdapter implements CircleMembershipPort {

  private final CircleMembershipRepository repository;

  public JpaCircleMembershipAdapter(CircleMembershipRepository repository) {
    this.repository = repository;
  }

  @Override
  public CircleMembership save(CircleMembership membership) {
    log.debug("save: upserting circle membership with circle type {}", membership.getCircleType());
    CircleMembershipId id =
        new CircleMembershipId(membership.getMemberIdA(), membership.getMemberIdB());

    CircleMembershipEntity entity =
        repository
            .findById(id)
            .orElseGet(
                () -> {
                  log.debug("save: no existing membership found, creating new entity");
                  CircleMembershipEntity e = new CircleMembershipEntity();
                  e.setId(id);
                  e.setMemberSince(OffsetDateTime.now());
                  return e;
                });

    entity.setCircleType(toEntityCircleType(membership.getCircleType()));
    CircleMembership saved = toDomain(repository.save(entity));
    log.debug("save: circle membership saved");
    return saved;
  }

  @Override
  public List<CircleMembership> findAllByMemberIdAndCircleType(
      UUID memberId, CircleType circleType) {
    log.debug("findAllByMemberIdAndCircleType: querying for circle type {}", circleType);
    List<CircleMembership> results =
        repository.findAllByMemberIdAndCircleType(memberId, toEntityCircleType(circleType)).stream()
            .map(this::toDomain)
            .toList();
    log.debug("findAllByMemberIdAndCircleType: found {} membership(s)", results.size());
    return results;
  }

  @Override
  public Optional<CircleMembership> findByPair(UUID memberIdA, UUID memberIdB) {
    log.debug("findByPair: querying membership");
    Optional<CircleMembership> result =
        repository.findByEitherPair(memberIdA, memberIdB).map(this::toDomain);
    log.debug("findByPair: membership {}", result.isPresent() ? "found" : "not found");
    return result;
  }

  // -------------------------------------------------------------------------
  // Mapping helpers
  // -------------------------------------------------------------------------

  private CircleMembership toDomain(CircleMembershipEntity entity) {
    var id = entity.getId();
    return CircleMembership.builder()
        .memberIdA(id.getMemberIdA())
        .memberIdB(id.getMemberIdB())
        .circleType(toDomainCircleType(entity.getCircleType()))
        .memberSince(entity.getMemberSince())
        .build();
  }

  private CircleTypeEntity toEntityCircleType(CircleType domain) {
    return switch (domain) {
      case TRUSTED -> CircleTypeEntity.TRUSTED;
      case CONNECTED -> CircleTypeEntity.CONNECTED;
    };
  }

  private CircleType toDomainCircleType(CircleTypeEntity entity) {
    return switch (entity) {
      case TRUSTED -> CircleType.TRUSTED;
      case CONNECTED -> CircleType.CONNECTED;
    };
  }
}
