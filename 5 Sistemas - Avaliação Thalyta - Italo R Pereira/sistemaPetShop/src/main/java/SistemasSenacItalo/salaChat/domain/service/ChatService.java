package SistemasSenacItalo.salaChat.domain.service;

import SistemasSenacItalo.salaChat.domain.entity.User;
import SistemasSenacItalo.salaChat.domain.entity.Message;
import SistemasSenacItalo.salaChat.domain.repository.ChatRepository;
import SistemasSenacItalo.salaChat.domain.dto.LoginDTO;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ChatService {

    private final ChatRepository repository;

    public ChatService(ChatRepository repository) {
        this.repository = repository;
    }

    public synchronized User login(LoginDTO login) {
        if (login == null || login.getUsername() == null || login.getEmail() == null) {
            throw new IllegalArgumentException("username e email são obrigatórios para login.");
        }

        String username = login.getUsername().trim();
        String email = login.getEmail().trim();

        Optional<User> existing = repository.findUserByUsername(username);
        if (existing.isPresent()) {
            // Já existe: verificar email (autenticidade)
            if (existing.get().getEmail().equalsIgnoreCase(email)) {
                return existing.get(); // já logado
            } else {
                throw new RuntimeException("Nome de usuário já em uso com email diferente.");
            }
        }

        if (!repository.canRegisterMoreUsers()) {
            throw new RuntimeException("Número máximo de usuários logados atingido.");
        }

        User created = new User(UUID.randomUUID(), username, email);
        return repository.saveUser(created);
    }

    public synchronized void logout(String username) {
        if (username == null) throw new IllegalArgumentException("username obrigatório para logout.");
        repository.removeUser(username);
    }

    public List<User> listLoggedUsers() {
        return repository.findAllLoggedUsers();
    }

    public Message sendMessage(String fromUsername, String toUsername, String content) {
        if (fromUsername == null || toUsername == null || content == null) {
            throw new IllegalArgumentException("from, to e content são obrigatórios.");
        }

        Optional<User> fromUser = repository.findUserByUsername(fromUsername);
        Optional<User> toUser = repository.findUserByUsername(toUsername);

        if (!fromUser.isPresent()) throw new RuntimeException("Remetente não está logado: " + fromUsername);
        if (!toUser.isPresent()) throw new RuntimeException("Destinatário não está logado: " + toUsername);

        Message msg = new Message(UUID.randomUUID(), fromUsername, toUsername, content.trim(), Instant.now());
        return repository.saveMessage(msg);
    }

    public List<Message> getMessagesForUser(String username) {
        return repository.findMessagesForUser(username);
    }
}
