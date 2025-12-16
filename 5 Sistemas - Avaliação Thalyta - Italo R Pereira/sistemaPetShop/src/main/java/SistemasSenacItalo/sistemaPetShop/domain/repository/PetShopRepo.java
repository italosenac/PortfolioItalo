package SistemasSenacItalo.sistemaPetShop.domain.repository;

import SistemasSenacItalo.sistemaPetShop.domain.entity.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PetShopRepo extends JpaRepository <Animal, UUID>{


}
