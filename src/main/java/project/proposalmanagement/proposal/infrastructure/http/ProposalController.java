package project.proposalmanagement.proposal.infrastructure.http;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.proposalmanagement.auth.domain.UserRole;
import project.proposalmanagement.auth.infrastructure.persistence.entity.User;
import project.proposalmanagement.proposal.application.CreateProposalUseCase;
import project.proposalmanagement.proposal.application.ListProposalUseCase;
import project.proposalmanagement.proposal.application.list.AccessScope;
import project.proposalmanagement.proposal.domain.Owner;
import project.proposalmanagement.proposal.domain.OwnerId;
import project.proposalmanagement.proposal.infrastructure.http.request.CreateProposalRequest;
import project.proposalmanagement.proposal.infrastructure.http.response.ProposalResponse;

import java.util.List;

@RestController
@RequestMapping("/proposals")
public class ProposalController {
    private CreateProposalUseCase createProposalUseCase;
    private ListProposalUseCase listProposalUseCase;

    public ProposalController(CreateProposalUseCase createProposalUseCase,
                              ListProposalUseCase listProposalUseCase) {
        this.createProposalUseCase = createProposalUseCase;
        this.listProposalUseCase = listProposalUseCase;
    }

    @PostMapping
    @PreAuthorize("hasRole('INFLUENCER')")
    public ProposalResponse createProposal(@RequestBody CreateProposalRequest request,
                                           @AuthenticationPrincipal User user) {
        var owner = new Owner(new OwnerId(user.getId()), user.getUsername());
        var output = this.createProposalUseCase.execute(request.toInput(), owner);

        return ProposalResponse.from(output);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('INFLUENCER', 'BRAND')")
    public List<ProposalResponse> findAllProposals(@AuthenticationPrincipal User user) {
        var accessScope = getAccessScope(user.getRole());
        var ownerId = new OwnerId(user.getId());

        return listProposalUseCase.execute(accessScope, ownerId)
                .stream()
                .map(ProposalResponse::from)
                .toList();
    }

    private static AccessScope getAccessScope(UserRole role) {
        return switch (role) {
            case ROLE_INFLUENCER -> AccessScope.OWN;
            case ROLE_BRAND -> AccessScope.ALL;
        };
    }
}
