package project.proposalmanagement.auth.infrastructure.http;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class Controller {

    @GetMapping
    public String hello(@AuthenticationPrincipal UserDetails user) { // Recupera informações de um usuario logado
        return "Hello World " + user.getUsername();
    }

    @GetMapping("/influencer")
    @PreAuthorize("hasRole('INFLUENCER')")//Apenas usuarios que possuem a ROLE INFLUENCER tem autenticação para acessar
    public String influencerEndpoint() {
        return "Hello INFLUENCER";
    }

    @GetMapping("/brand")
    @PreAuthorize("hasRole('BRAND')")//Apenas usuarios que possuem a ROLE BRAND tem autenticação para acessar
    public String brandEndpoint() {
        return "Hello BRAND";
    }
}
