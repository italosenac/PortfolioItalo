package SistemasSenacItalo.salaChat.domain.entity;

import java.time.Instant;
import java.util.UUID;

public class Message {
    private final UUID id;
    private final String fromUsername;
    private final String toUsername;
    private final String content;
    private final Instant sentAt;

    public Message(UUID id, String fromUsername, String toUsername, String content, Instant sentAt) {
        this.id = id;
        this.fromUsername = fromUsername;
        this.toUsername = toUsername;
        this.content = content;
        this.sentAt = sentAt;
    }

    public UUID getId() { return id; }
    public String getFromUsername() { return fromUsername; }
    public String getToUsername() { return toUsername; }
    public String getContent() { return content; }
    public Instant getSentAt() { return sentAt; }

    @Override
    public String toString() {
        return "[" + sentAt + "] " + fromUsername + " -> " + toUsername + ": " + content;
    }
}
