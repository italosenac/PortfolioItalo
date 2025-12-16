package facsenacMaven.sistemaChat.domain.ApplicationService;

import facsenacMaven.sistemaChat.domain.repositories.AlunoRepository;
import facsenacMaven.sistemaChat.domain.domainExceptions.AlunoNotFound;
import facsenacMaven.sistemaChat.domain.domainExceptions.InvalidCredentialsException;
import facsenacMaven.sistemaChat.domain.entity.Aluno;
import facsenacMaven.sistemaChat.domain.entity.Message;
import facsenacMaven.sistemaChat.infrastructure.dto.CadastroAlunoDTO;
import facsenacMaven.sistemaChat.infrastructure.dto.LoginDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository alunoRepo;
    private final PasswordEncoder passwordEncoder;
    private final SessionService sessionService;
    private final MessageService messageService;

    private int usuariosLogados = 0;
    private static final int LIMITE_USUARIOS = 10;
    private static final Logger LOGGER = LoggerFactory.getLogger(AlunoService.class);

    @Transactional
    public Aluno createAluno(CadastroAlunoDTO dto) {
        if (dto == null
                || dto.getName() == null || dto.getName().isBlank()
                || dto.getEmail() == null || dto.getEmail().isBlank()
                || dto.getSecret() == null || dto.getSecret().isBlank()) {
            throw new IllegalArgumentException("Nome, email e senha são obrigatórios!");
        }

        Aluno aluno = Aluno.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .secret(passwordEncoder.encode(dto.getSecret()))
                .deleted(false)
                .build();

        alunoRepo.save(aluno);
        LOGGER.info("Aluno cadastrado com ID: {}", aluno.getId());
        return aluno;
    }

    public Aluno loadAluno(String id) {
        return alunoRepo.findByIdAndDeleted(UUID.fromString(id), false)
                .orElseThrow(() -> new AlunoNotFound(id));
    }
    private void validarCredenciais(LoginDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Dados de login obrigatórios!");
        }
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new IllegalArgumentException("Nome de usuário obrigatório!");
        }
        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email obrigatório!");
        }
        if (dto.getSecret() == null || dto.getSecret().isBlank()) {
            throw new IllegalArgumentException("Senha obrigatória!");
        }
    }

    @Transactional
    public Aluno login(LoginDTO logindto) {
        validarCredenciais(logindto);

        if (usuariosLogados >= LIMITE_USUARIOS) {
            throw new IllegalStateException("Limite máximo de usuários logados atingido!");
        }

        Aluno aluno = alunoRepo.findByEmailAndDeleted(logindto.getEmail(), false)
                .orElseThrow(() -> new AlunoNotFound(logindto.getEmail()));

        if (!senhaCorreta(logindto.getSecret(), aluno.getSecret())) {
            LOGGER.warn("Falha de login para o email: {}", logindto.getEmail());
            throw new InvalidCredentialsException("Email ou senha inválidos!");
        }
        usuariosLogados++;
        boolean registered = sessionService.register(aluno.getEmail());
        if (!registered && usuariosLogados > 0) {
            usuariosLogados--;
            throw new IllegalStateException("Não foi possível registrar sessão (limite atingido ou já logado)");
        }

        LOGGER.info("Aluno autenticado: {} — logados: {}", aluno.getId(), usuariosLogados);
        return aluno;
    }

    public void logout(String alunoId) {
        if (alunoId == null) return;
        Aluno aluno = loadAluno(alunoId);
        logoutByEmail(aluno.getEmail());
    }

    public void logoutByEmail(String email) {
        if (email == null) return;
        boolean removed = sessionService.unregister(email);
        if (removed && usuariosLogados > 0) {
            usuariosLogados--;
            LOGGER.info("Aluno deslogado: {} — logados: {}", email, usuariosLogados);
        } else if (!removed) {
            LOGGER.warn("Logout solicitado para usuário não logado: {}", email);
        }
    }

    public Message enviarMensagem(String senderEmail, String recipientEmail, String content) {
        if (!sessionService.isLogged(senderEmail)) {
            throw new IllegalStateException("Remetente não está logado");
        }
        Aluno destinatario = alunoRepo.findByEmailAndDeleted(recipientEmail, false)
                .orElseThrow(() -> new AlunoNotFound(recipientEmail));
        return messageService.sendMessage(senderEmail, destinatario.getEmail(), content);
    }

    public List<String> listarUsuariosLogados() {
        return sessionService.listLogged();
    }

    private boolean senhaCorreta(String senhaInformada, String senhaArmazenada) {
        if (senhaArmazenada == null) return false;
        if (isHashed(senhaArmazenada)) {
            return passwordEncoder.matches(senhaInformada, senhaArmazenada);
        }
        return senhaInformada.equals(senhaArmazenada);
    }

    private boolean isHashed(String senha) {
        if (senha == null) return false;
        if (senha.length() > 50) return true;
        return senha.startsWith("$2a$") || senha.startsWith("$2b$") || senha.startsWith("{bcrypt}");
    }
}