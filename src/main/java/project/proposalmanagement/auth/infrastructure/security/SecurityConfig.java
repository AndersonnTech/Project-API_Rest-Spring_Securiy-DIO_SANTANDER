package project.proposalmanagement.auth.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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

        //              .formLogin(Customizer.withDefaults()); // Formulário de login;

        return http.build();
    }

    @Bean
    UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        /*UserDetails user = User.withDefaultPasswordEncoder()
                .username("influencer")
                .password("password")
                .roles("INFLUENCER")
                .build();
        */
        UserDetails user = User.withUsername("influencer")
                .password(passwordEncoder.encode("password"))
                .roles("INFLUENCER")
                .build();

        UserDetails brand = User.withUsername("brand")
                .password(passwordEncoder.encode("password"))
                .roles("BRAND")
                .build();

        return new InMemoryUserDetailsManager(user, brand);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();// criptografia escolhida para a senha.
    }
}
