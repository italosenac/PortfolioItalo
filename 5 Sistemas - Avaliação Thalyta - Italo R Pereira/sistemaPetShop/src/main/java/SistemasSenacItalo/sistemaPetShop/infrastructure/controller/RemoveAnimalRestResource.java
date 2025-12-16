package SistemasSenacItalo.sistemaPetShop.infrastructure.controller;

import SistemasSenacItalo.sistemaPetShop.domain.entity.Animal;
import SistemasSenacItalo.sistemaPetShop.domain.service.PetShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static SistemasSenacItalo.sistemaPetShop.infrastructure.controller.RestConstants.PATH_REMOVE_ANIMAL;

@RestController
@RequestMapping(PATH_REMOVE_ANIMAL)
@RequiredArgsConstructor
public class RemoveAnimalRestResource {

    private final PetShopService removeAnimalService;

    @DeleteMapping
    public ResponseEntity<String> removeAnimal(@RequestBody Animal removeAnimalData){

        removeAnimalService.removeAnimal(removeAnimalData);

        return ResponseEntity.ok("Deletado com sucesso!");
    }
}

