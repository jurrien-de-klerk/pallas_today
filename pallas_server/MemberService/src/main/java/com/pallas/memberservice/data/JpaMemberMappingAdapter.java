package com.pallas.memberservice.data;

import com.pallas.memberservice.domain.MemberMapping;
import com.pallas.memberservice.domain.MemberMappingPort;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * JPA adapter for {@link MemberMappingPort}. Translates between the JPA entity and the domain
 * record.
 */
@Component
public class JpaMemberMappingAdapter implements MemberMappingPort {

  private final MemberIdentityMappingRepository repository;

  public JpaMemberMappingAdapter(MemberIdentityMappingRepository repository) {
    this.repository = repository;
  }

  @Override
  public Optional<MemberMapping> findBySub(String keycloakSub) {
    return repository.findByKeycloakSub(keycloakSub).map(this::toDomain);
  }

  @Override
  public Optional<MemberMapping> findByMemberId(UUID memberId) {
    return repository.findById(memberId).map(this::toDomain);
  }

  @Override
  public List<MemberMapping> findByMemberIds(List<UUID> memberIds) {
    return repository.findAllById(memberIds).stream().map(this::toDomain).toList();
  }

  @Override
  public MemberMapping save(MemberMapping mapping) {
    MemberIdentityMapping entity =
        new MemberIdentityMapping(mapping.memberId(), mapping.keycloakSub(), mapping.createdAt());
    return toDomain(repository.save(entity));
  }

  private MemberMapping toDomain(MemberIdentityMapping entity) {
    return new MemberMapping(entity.getMemberId(), entity.getKeycloakSub(), entity.getCreatedAt());
  }
}
