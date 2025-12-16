package SistemasSenacItalo.sistemaPetShop.domain.entity;

import lombok.*;
import jakarta.persistence.*;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "ANIMAIS_TABLE")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, length = 36)
    private UUID id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "especie", nullable = false, length = 100)
    private String especie;

    @Column(name = "deleted")
    private boolean deleted;

}
