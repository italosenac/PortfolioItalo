package facsenacMaven.sistemaChat.domain.ApplicationService;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionService {

    private final Set<String> loggedEmails = ConcurrentHashMap.newKeySet();
    private static final int MAX_LOGGED = 10;

    public boolean register(String email) {
        if (email == null) return false;
        if (loggedEmails.size() >= MAX_LOGGED) return false;
        return loggedEmails.add(email);
    }

    public boolean unregister(String email) {
        if (email == null) return false;
        return loggedEmails.remove(email);
    }

    public boolean isLogged(String email) {
        if (email == null) return false;
        return loggedEmails.contains(email);
    }

    public List<String> listLogged() {
        return Collections.list(Collections.enumeration(loggedEmails));
    }

    public int count() {
        return loggedEmails.size();
    }
}
