package SistemasSenacItalo.salaChat.domain.dto;

public class MessageDTO {
    private String toUsername;
    private String content;

    public MessageDTO() {}

    public MessageDTO(String toUsername, String content) {
        this.toUsername = toUsername;
        this.content = content;
    }

    public String getToUsername() { return toUsername; }
    public void setToUsername(String toUsername) { this.toUsername = toUsername; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
