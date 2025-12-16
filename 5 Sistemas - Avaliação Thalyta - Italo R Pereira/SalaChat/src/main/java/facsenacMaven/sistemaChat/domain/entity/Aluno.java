package facsenacMaven.sistemaChat.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Data //CRIAR GETTERS E SETTERS VIA LOMBOK APELAO SÓ COM @ (TIPO UM FRAMEWORK)
@AllArgsConstructor //CRIAR CONSTRUTOR COM ARGUMENTOS
@NoArgsConstructor //CRIAR CONSTRUTOR VAZIO PARA SER MANIPULADO PELA INTERFACE DO JPA (JAKARTA PERSISTENTE APPLICATION)
@Builder //CRIAR OBJETO MEMBERS
@Entity //CRIAR ENTIDADE MEMBERS PARA SER MANIPULADO NO BANCO DE DADOS
@Table(name = "MEMBERS_TABLE")
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "Member_Id", nullable = false, length = 36)
    private UUID id;

    @Column(name = "Member_Name", nullable = false, length = 100)
    private String name;

    @Column(name = "Member_Secret",  length = 100)
    private String secret;
    @Column(name = "Member_Email", length = 100)
    private String email;
    @Column(name = "Deleted")
    private boolean deleted;

}
