package facsenacMaven.sistemaChat.infrastructure.config;

import facsenacMaven.sistemaChat.domain.repositories.AlunoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(AlunoRepository alunoRepo) {
        return email -> alunoRepo.findByEmailAndDeleted(email, false)
                .map(aluno -> User.withUsername(aluno.getEmail())
                        .password(aluno.getSecret())
                        .roles("USER")
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/alunos").permitAll()
                        .requestMatchers(HttpMethod.POST, "/alunos/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/alunos/logout").permitAll()
                        .requestMatchers("/alunos/session/**").permitAll()
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers.disable())
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
