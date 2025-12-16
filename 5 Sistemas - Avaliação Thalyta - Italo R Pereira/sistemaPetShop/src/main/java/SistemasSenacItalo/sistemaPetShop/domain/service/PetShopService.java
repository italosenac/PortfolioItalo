package SistemasSenacItalo.sistemaPetShop.domain.service;

import SistemasSenacItalo.sistemaPetShop.domain.entity.Animal;
import SistemasSenacItalo.sistemaPetShop.domain.repository.PetShopRepo;
import SistemasSenacItalo.sistemaPetShop.infrastructure.dto.AnimalDataDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetShopService {

    private final PetShopRepo petShopRepo;

    @Transactional
    public Animal saveNewAnimal(AnimalDataDTO dataAnimal) {
        Animal animal = Animal.builder()
                .nome(dataAnimal.getNome())
                .especie(dataAnimal.getEspecie().toUpperCase())
                .build();

        return petShopRepo.save(animal);
    }

    @Transactional
    public void removeAnimal(Animal dataAnimal) {
        validarParametros(dataAnimal);

        String nome = dataAnimal.getNome();
        String especie = normalizarEspecie(dataAnimal.getEspecie());

        removerAnimalDoBanco(nome, especie);
    }

    private void validarParametros(Animal dataAnimal) {
        if (dataAnimal == null || dataAnimal.getNome() == null || dataAnimal.getEspecie() == null) {
            throw new IllegalArgumentException("O nome e a espécie do animal são obrigatórios para remoção.");
        }
    }

    private String normalizarEspecie(String especie) {
        return especie.toUpperCase();
    }

    private void removerAnimalDoBanco(String nome, String especieNormalizada) {
        List<Animal> animais = petShopRepo.findAll();

        for (Animal animal : animais) {
            boolean mesmoNome = animal.getNome().equalsIgnoreCase(nome);
            boolean mesmaEspecie = animal.getEspecie().equals(especieNormalizada);

            if (mesmoNome && mesmaEspecie) {
                petShopRepo.delete(animal);
                return;
            }
        }

        throw new RuntimeException("Nenhum animal encontrado com nome: " + nome + " e espécie: " + especieNormalizada);
    }
}