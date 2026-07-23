package project.proposalmanagement.proposal.application;

import org.springframework.stereotype.Service;
import project.proposalmanagement.proposal.application.list.AccessScope;
import project.proposalmanagement.proposal.application.list.Factory;
import project.proposalmanagement.proposal.application.output.ProposalOutput;
import project.proposalmanagement.proposal.domain.OwnerId;

import java.util.List;

@Service
public class ListProposalUseCase {
    private final Factory factory;

    public ListProposalUseCase(Factory factory) {
        this.factory = factory;
    }

    public List<ProposalOutput> execute(AccessScope scope, OwnerId ownerId) {
        var proposals = factory.getStrategy(scope).getProposals(ownerId);

        return proposals.stream().map(ProposalOutput::from).toList();
    }
}
