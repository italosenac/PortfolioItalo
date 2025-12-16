package SistemasSenacItalo.sistemaPetShop.infrastructure.controller;

import SistemasSenacItalo.sistemaPetShop.domain.entity.Animal;
import SistemasSenacItalo.sistemaPetShop.domain.repository.PetShopRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static SistemasSenacItalo.sistemaPetShop.infrastructure.controller.RestConstants.PATH_CONSULTA;

@RestController
@RequestMapping(PATH_CONSULTA)
@RequiredArgsConstructor
public class FindAnimalRestResource {

    private final PetShopRepo petShopRepo;

    @GetMapping("/{id}")
    public ResponseEntity<Animal> buscarPorId(@PathVariable UUID id) {
        return petShopRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}