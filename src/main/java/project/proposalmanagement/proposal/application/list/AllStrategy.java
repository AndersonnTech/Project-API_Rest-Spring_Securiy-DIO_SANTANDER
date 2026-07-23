package project.proposalmanagement.proposal.application.list;

import org.springframework.stereotype.Service;
import project.proposalmanagement.proposal.domain.OwnerId;
import project.proposalmanagement.proposal.domain.Proposal;
import project.proposalmanagement.proposal.domain.ProposalRepository;

import java.util.List;

@Service
public class AllStrategy implements Strategy{
    private final ProposalRepository proposalRepository;

    public AllStrategy(ProposalRepository proposalRepository) {
        this.proposalRepository = proposalRepository;
    }

    @Override
    public List<Proposal> getProposals(OwnerId ownerId) {
        return proposalRepository.findAll();
    }

    @Override
    public AccessScope getScope() {
        return AccessScope.ALL;
    }
}
