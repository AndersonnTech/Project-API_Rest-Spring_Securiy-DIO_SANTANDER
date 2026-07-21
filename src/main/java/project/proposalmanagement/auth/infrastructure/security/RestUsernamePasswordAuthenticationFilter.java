package project.proposalmanagement.auth.infrastructure.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component //Spring lê essa classe quando é iniciado;
public class RestUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    //Mapeia json para objeto e vice-versa;
    private final ObjectMapper objectMapper;

    public RestUsernamePasswordAuthenticationFilter(AuthenticationConfiguration authenticationConfiguration,
                                                    ObjectMapper objectMapper) {
        //implementa o atenticador padrão
        super(authenticationConfiguration.getAuthenticationManager());
        this.objectMapper = objectMapper;
        //nova url para login
        setFilterProcessesUrl("/api/auth/login");

        //Configura o autenticador para que não redirecione a requisição para a pagina padrão de FormLogin, retorna apenas um 200OK
        setAuthenticationSuccessHandler((HttpServletRequest request, HttpServletResponse response, Authentication authentication) ->
                response.setStatus(HttpServletResponse.SC_OK));
    }

    //Configuração de como sera feita a authenticação
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            var loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);

            // recebe os parametros sem autenticação para gerar o token
            var token = UsernamePasswordAuthenticationToken.unauthenticated(
                    loginRequest.username(),
                    loginRequest.password());

            return getAuthenticationManager().authenticate(token);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Dto que recebe os parametros por request
    public record LoginRequest(String username, String password){}
}
