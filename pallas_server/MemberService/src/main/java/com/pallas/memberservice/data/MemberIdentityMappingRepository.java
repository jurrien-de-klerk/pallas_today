package com.pallas.memberservice.data;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberIdentityMappingRepository
    extends JpaRepository<MemberIdentityMapping, UUID> {

  Optional<MemberIdentityMapping> findByKeycloakSub(String keycloakSub);
}
