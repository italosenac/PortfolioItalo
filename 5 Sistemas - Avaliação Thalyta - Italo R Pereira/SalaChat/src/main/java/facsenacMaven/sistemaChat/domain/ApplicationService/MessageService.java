package facsenacMaven.sistemaChat.domain.ApplicationService;

import facsenacMaven.sistemaChat.domain.repositories.MessageRepository;
import facsenacMaven.sistemaChat.domain.repositories.AlunoRepository;
import facsenacMaven.sistemaChat.domain.domainExceptions.AlunoNotFound;
import facsenacMaven.sistemaChat.domain.domainExceptions.InvalidCredentialsException;
import facsenacMaven.sistemaChat.domain.entity.Message;
import facsenacMaven.sistemaChat.domain.entity.Aluno;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepo;
    private final AlunoRepository alunoRepo;
    private final SessionService sessionService;

    @Transactional
    public Message sendMessage(String senderEmail, String receiverEmail, String content) {
        if (senderEmail == null || receiverEmail == null || content == null || content.isBlank()) {
            throw new IllegalArgumentException("Remetente, destinatário e conteúdo são obrigatórios");
        }

        if (!sessionService.isLogged(senderEmail)) {
            throw new InvalidCredentialsException("Remetente não está logado");
        }

        if (!sessionService.isLogged(receiverEmail)) {
            throw new InvalidCredentialsException("Destinatário não está logado");
        }

        Aluno sender = alunoRepo.findByEmail(senderEmail)
                .orElseThrow(() -> new AlunoNotFound(senderEmail));
        Aluno receiver = alunoRepo.findByEmail(receiverEmail)
                .orElseThrow(() -> new AlunoNotFound(receiverEmail));

        Message message = Message.builder()
                .content(content)
                .sender(sender.getName())
                .receiver(receiver.getName())
                .messageDate(LocalDateTime.now())
                .deleted(false)
                .build();

        return messageRepo.save(message);
    }

    public List<Message> inbox(String receiverEmail) {
        if (receiverEmail == null) return List.of();
        return messageRepo.findByReceiverAndDeletedOrderByMessageDateDesc(receiverEmail, false);
    }

    public List<Message> sent(String senderEmail) {
        if (senderEmail == null) return List.of();
        return messageRepo.findBySenderAndDeleted(senderEmail, false);
    }
}
