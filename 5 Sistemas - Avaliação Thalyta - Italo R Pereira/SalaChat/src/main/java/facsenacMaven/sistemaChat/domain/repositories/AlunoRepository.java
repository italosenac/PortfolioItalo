package facsenacMaven.sistemaChat.domain.repositories;

import facsenacMaven.sistemaChat.domain.entity.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, UUID> {

    Optional<Aluno> findByIdAndDeleted(UUID id, Boolean deleted);

    Optional<Aluno> findByEmail(String email);

    Optional<Aluno> findByEmailAndDeleted(String email, Boolean deleted);

}
