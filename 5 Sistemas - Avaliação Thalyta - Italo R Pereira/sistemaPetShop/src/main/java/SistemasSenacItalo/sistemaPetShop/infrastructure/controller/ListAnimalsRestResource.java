package SistemasSenacItalo.sistemaPetShop.infrastructure.controller;

import SistemasSenacItalo.sistemaPetShop.domain.repository.PetShopRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static SistemasSenacItalo.sistemaPetShop.infrastructure.controller.RestConstants.PATH_LISTA;

@RestController
@RequestMapping(PATH_LISTA)
@RequiredArgsConstructor
public class ListAnimalsRestResource {

    private final PetShopRepo petShopRepo;

    @GetMapping
    @SuppressWarnings("unused")
    public ResponseEntity<?> listarTodos() {
        var animais = petShopRepo.findAll();
        return animais.isEmpty()?
                ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Nenhum animal encontrado no sistema.")
                : ResponseEntity.ok(animais);
    }


}