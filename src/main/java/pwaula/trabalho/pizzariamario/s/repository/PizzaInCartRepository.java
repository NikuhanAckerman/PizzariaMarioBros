package pwaula.trabalho.pizzariamario.s.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pwaula.trabalho.pizzariamario.s.model.PizzaEntity;
import pwaula.trabalho.pizzariamario.s.model.PizzaInCartEntity;

import java.util.List;

@Repository
public interface PizzaInCartRepository extends MongoRepository<PizzaInCartEntity, String> {
    List<PizzaInCartEntity> getPizzaInCartEntitiesByCartId(String cartId);
}
