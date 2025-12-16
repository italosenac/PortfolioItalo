package SistemasSenacItalo.salaChat.domain.repository;

import SistemasSenacItalo.salaChat.domain.entity.User;
import SistemasSenacItalo.salaChat.domain.entity.Message;

import java.util.List;
import java.util.Optional;

public interface ChatRepository {
    boolean canRegisterMoreUsers();
    Optional<User> findUserByUsername(String username);
    Optional<User> findUserByUsernameAndEmail(String username, String email);
    User saveUser(User user);
    void removeUser(String username);
    List<User> findAllLoggedUsers();

    Message saveMessage(Message message);
    List<Message> findMessagesForUser(String username);
    List<Message> findAllMessages();
}
