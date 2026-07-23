package project.proposalmanagement.proposal.infrastructure.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import project.proposalmanagement.proposal.infrastructure.persistence.entity.ProposalEntity;

import java.util.List;
import java.util.UUID;

public interface ProposalEntityRepository extends CrudRepository<ProposalEntity, UUID> {
    List<ProposalEntity> findAllByOwnerId(UUID ownerId);
}
