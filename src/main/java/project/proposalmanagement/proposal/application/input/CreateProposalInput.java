package project.proposalmanagement.proposal.application.input;

import project.proposalmanagement.proposal.domain.Owner;
import project.proposalmanagement.proposal.domain.Proposal;

import java.util.Optional;

public record CreateProposalInput(String title, Optional<String> description) {
    public Proposal toDomain(Owner owner) {
        return new Proposal(title, description, owner);
    }
}
