package SistemasSenacItalo.sistemaPetShop.infrastructure.controller;

import SistemasSenacItalo.sistemaPetShop.domain.entity.Animal;
import SistemasSenacItalo.sistemaPetShop.domain.service.PetShopService;
import SistemasSenacItalo.sistemaPetShop.infrastructure.dto.AnimalDataDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;
import static SistemasSenacItalo.sistemaPetShop.infrastructure.controller.RestConstants.PATH_CADASTRO;

@RestController
@RequestMapping(PATH_CADASTRO)
@RequiredArgsConstructor
public class SaveAnimalRestResource {

    private final PetShopService saveAnimalService;
    @PostMapping
    public ResponseEntity<AnimalDataDTO> saveAnimal(@RequestBody AnimalDataDTO SaveAnimalData){

        Animal animal = saveAnimalService.saveNewAnimal(SaveAnimalData);

        return ResponseEntity
                .created(URI.create(PATH_CADASTRO + "/" + animal.getId()))
                .body(AnimalDataDTO.saveAnimal(animal));

    }
}
