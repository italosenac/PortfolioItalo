package facsenacMaven.sistemaChat.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MESSAGES_TABLE")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "Message_Content", nullable = false)
    private String content;

    @Column(name = "Message_From", nullable = false)
    private String sender;

    @Column(name = "Message_To", nullable = false)
    private String receiver;

    @Column(name = "Message_Date", nullable = false)
    private LocalDateTime messageDate;

    @Column(name = "Message_Deleted")
    private boolean deleted;
}
