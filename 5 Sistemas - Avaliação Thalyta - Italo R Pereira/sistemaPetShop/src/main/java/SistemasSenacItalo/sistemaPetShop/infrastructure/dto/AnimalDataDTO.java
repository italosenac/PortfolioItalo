package SistemasSenacItalo.sistemaPetShop.infrastructure.dto;

import SistemasSenacItalo.sistemaPetShop.domain.entity.Animal;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnimalDataDTO {

    private UUID id;

    @NotNull(message = "O nome não pode estar vazio!")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters!")
    private String nome;

    @NotNull(message = "Espécie não informada!")
    private String especie;

    public static AnimalDataDTO saveAnimal(Animal animal) {
        return new AnimalDataDTO(
                animal.getId(),
                animal.getNome(),
                animal.getEspecie()
        );
    }
}
