package facsenacMaven.sistemaChat.infrastructure.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@Data
public class MessageDataDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private final UUID id;

    @NotBlank(message = "Content cannot be empty!")
    private final String content;

    @NotBlank(message = "Sender cannot be empty!")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters!")
    @Email(message = "Invalid email address!")
    private final String sender;

    @NotBlank(message = "Receiver cannot be empty!")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters!")
    @Email(message = "Invalid email address!")
    private final String receiver;

    @Column(name = "message_date", nullable = false)
    private final LocalDate message_date;

    @Column(name = "Message_Deleted")
    private final boolean deleted;

}
