package project.proposalmanagement.auth.infrastructure.security;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import project.proposalmanagement.auth.domain.UserRole;
import project.proposalmanagement.auth.infrastructure.persistence.entity.User;
import project.proposalmanagement.auth.infrastructure.persistence.repository.UserRepository;

import java.util.List;

@Configuration
@EnableWebSecurity //Aplicação Spring irá usar o Spring Security
@EnableMethodSecurity //Permite anotações e métodos para maior segurança.
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, RestUsernamePasswordAuthenticationFilter restUsernamePasswordAuthenticationFilter) throws Exception { //SecurityFilterChain Ponto de Entrada de Segurança
        http
                .csrf(AbstractHttpConfigurer::disable)//desabilita o csrf para simplificar a aplicação

                //Configuração para salvar a sessão de login requisitada
                .securityContext(context -> context.requireExplicitSave(false))

                .authorizeHttpRequests(auth -> auth

                        //Habilita a nova url de login atraves de request por json/REST
                        .requestMatchers("/api/auth/login").permitAll()
                        .anyRequest().authenticated()) // Todas as requisições terão que ser autenticadas;

                //Substitui a implementação do FormLogin, pela implementação customizada de requisições REST.
                .addFilterAt(restUsernamePasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();// criptografia escolhida para a senha.
    }

    @Bean
    CommandLineRunner initDatabase(UserRepository repository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (repository.count() == 0) {
                User fitnessInfluencer = new User();
                fitnessInfluencer.setUsername("fitness_vibe");
                fitnessInfluencer.setPassword(passwordEncoder().encode("password"));
                fitnessInfluencer.setRole(UserRole.ROLE_INFLUENCER);

                User techInfluencer = new User();
                techInfluencer.setUsername("tech_guru");
                techInfluencer.setPassword(passwordEncoder().encode("password"));
                techInfluencer.setRole(UserRole.ROLE_INFLUENCER);

                User brand = new User();
                brand.setUsername("logistics");
                brand.setPassword(passwordEncoder().encode("password"));
                brand.setRole(UserRole.ROLE_BRAND);

                repository.saveAll(List.of(fitnessInfluencer, techInfluencer, brand));
            }
        };
    }
}
