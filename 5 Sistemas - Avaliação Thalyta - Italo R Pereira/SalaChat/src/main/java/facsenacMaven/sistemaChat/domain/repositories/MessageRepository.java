package facsenacMaven.sistemaChat.domain.repositories;

import facsenacMaven.sistemaChat.domain.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository

public interface MessageRepository extends JpaRepository<Message, UUID> {

    List<Message> findBySenderAndDeleted(String sender, boolean deleted);

    List<Message> findByReceiverAndDeletedOrderByMessageDateDesc(String receiver, boolean deleted);

}
