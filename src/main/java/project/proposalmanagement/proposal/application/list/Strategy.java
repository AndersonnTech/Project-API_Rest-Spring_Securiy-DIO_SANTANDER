package project.proposalmanagement.proposal.application.list;

import project.proposalmanagement.proposal.domain.OwnerId;
import project.proposalmanagement.proposal.domain.Proposal;

import java.util.List;

public interface Strategy {
    List<Proposal> getProposals(OwnerId ownerId);
    AccessScope getScope();
}
