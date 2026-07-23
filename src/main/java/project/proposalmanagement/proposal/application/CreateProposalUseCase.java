package project.proposalmanagement.proposal.application;

import org.springframework.stereotype.Service;
import project.proposalmanagement.proposal.application.input.CreateProposalInput;
import project.proposalmanagement.proposal.application.output.ProposalOutput;
import project.proposalmanagement.proposal.domain.Owner;
import project.proposalmanagement.proposal.domain.ProposalRepository;

@Service
public class CreateProposalUseCase {
    private final ProposalRepository proposalRepository;

    public CreateProposalUseCase(ProposalRepository proposalRepository) {
        this.proposalRepository = proposalRepository;
    }

    public ProposalOutput execute(CreateProposalInput input, Owner owner){
        var proposal = input.toDomain(owner);
        var saved = proposalRepository.save(proposal);

        return ProposalOutput.from(saved);
    }
}
