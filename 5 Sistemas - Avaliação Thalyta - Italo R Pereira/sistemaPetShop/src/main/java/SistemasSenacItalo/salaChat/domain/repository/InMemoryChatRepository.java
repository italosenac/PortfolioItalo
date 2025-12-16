package SistemasSenacItalo.salaChat.domain.repository;

import SistemasSenacItalo.salaChat.domain.entity.User;
import SistemasSenacItalo.salaChat.domain.entity.Message;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class InMemoryChatRepository implements ChatRepository {

    private final Map<String, User> loggedUsers = new ConcurrentHashMap<>(); // key: username lowercase
    private final List<Message> messages = new CopyOnWriteArrayList<>();
    private final int MAX_USERS;

    public InMemoryChatRepository() {
        this.MAX_USERS = 10; // padrão; pode ser sobrescrito por configuração futura
    }

    public InMemoryChatRepository(int maxUsers) {
        this.MAX_USERS = maxUsers;
    }

    @Override
    public boolean canRegisterMoreUsers() {
        return loggedUsers.size() < MAX_USERS;
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        if (username == null) return Optional.empty();
        return Optional.ofNullable(loggedUsers.get(username.toLowerCase()));
    }

    @Override
    public Optional<User> findUserByUsernameAndEmail(String username, String email) {
        return findUserByUsername(username)
                .filter(u -> u.getEmail().equalsIgnoreCase(email));
    }

    @Override
    public User saveUser(User user) {
        loggedUsers.put(user.getUsername().toLowerCase(), user);
        return user;
    }

    @Override
    public void removeUser(String username) {
        if (username != null) loggedUsers.remove(username.toLowerCase());
    }

    @Override
    public List<User> findAllLoggedUsers() {
        return new ArrayList<>(loggedUsers.values());
    }

    @Override
    public Message saveMessage(Message message) {
        messages.add(message);
        return message;
    }

    @Override
    public List<Message> findMessagesForUser(String username) {
        List<Message> res = new ArrayList<>();
        for (Message m : messages) {
            if (m.getToUsername().equalsIgnoreCase(username) || m.getFromUsername().equalsIgnoreCase(username)) {
                res.add(m);
            }
        }
        return res;
    }

    @Override
    public List<Message> findAllMessages() {
        return new ArrayList<>(messages);
    }
}
