package project.proposalmanagement.proposal.infrastructure.persistence.repository;

import org.springframework.stereotype.Repository;
import project.proposalmanagement.proposal.domain.OwnerId;
import project.proposalmanagement.proposal.domain.Proposal;
import project.proposalmanagement.proposal.domain.ProposalRepository;
import project.proposalmanagement.proposal.infrastructure.persistence.entity.ProposalEntity;

import java.util.List;
import java.util.stream.StreamSupport;

@Repository
public class JpaProposalRepository implements ProposalRepository {
    private final ProposalEntityRepository proposalEntityRepository;

    public JpaProposalRepository(ProposalEntityRepository proposalEntityRepository) {
        this.proposalEntityRepository = proposalEntityRepository;
    }

    @Override
    public List<Proposal> findAll() {
        var iterable =  proposalEntityRepository.findAll();
        return StreamSupport
                .stream(iterable.spliterator(), false)
                .map(ProposalEntity::toDomain)
                .toList();
    }

    @Override
    public List<Proposal> findAllByOwnerId(OwnerId ownerId) {
        return proposalEntityRepository.findAllByOwnerId(ownerId.id())
                .stream()
                .map(ProposalEntity::toDomain)
                .toList();
    }

    @Override
    public Proposal save(Proposal proposal) {
        var entity =ProposalEntity.from(proposal);
        var saved = proposalEntityRepository.save(entity);
        return saved.toDomain();
    }
}
