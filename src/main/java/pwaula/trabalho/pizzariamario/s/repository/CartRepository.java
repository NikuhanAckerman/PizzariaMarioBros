package pwaula.trabalho.pizzariamario.s.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pwaula.trabalho.pizzariamario.s.model.CartEntity;
import pwaula.trabalho.pizzariamario.s.model.PizzaInCartEntity;

@Repository
public interface CartRepository extends MongoRepository<CartEntity, String> {

}
